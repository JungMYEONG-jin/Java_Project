console.log("dirnaem : " + __dirname);

var express = require('express');
const Config = require('./server_cfg');
const log = require('./server_log');
const fs = require('fs');
const https = require('https');
// const httpsClient = require('http');
const WebSocket = require('ws');
const WebSocketServer = WebSocket.Server;
// 메시지 타입
const TYPE_MSG = "MSG";		// 클라이언트 -> 클라이언트 메시지 또는 서버 -> 클라이언트 메시지 (메시지용)
const TYPE_SVR = "SVR";		// 서버 -> 클라이언트 메시지 (시스템적인 메시지)
const TYPE_INIT = "INIT";		// 클라이언트 -> 서버 : 나의 정보 초기화
const TYPE_INIT_FAIL = "INIT_FAIL";	// 서버 -> 클라이언트 : 정보 초기화 실패
const TYPE_CLOSE = "CLOSE";		// 서버 -> 클라이언트 : 방에서 빠져나감
const TYPE_CONN = "CONNECTED"; // 서버 -> 클라이언트 : 접속됨
const TYPE_PING = "PING"; 		// TIMEOUT 되지 않도록 PING 발송
const TYPE_SPASS = "SPASS";		// 영상관련 정보
const TYPE_RECV_OK = "RECV_OK";		// 수신 완료
const TYPE_LOG = "LOG";		// 로그 출력용

// 서버간 메시지 타입
const TYPE_SVR_COM_MSG = "SVR_COM_MSG";
const TYPE_SVR_COM_OK = "SVR_COM_OK";
const TYPE_SVR_FND_MEM = "SVR_FND_MEM";
const TYPE_SVR_FND_MEM_OK = "SVR_FND_MEM_OK";

const localIp = require("ip").address();
const myHost = require("os").hostname();
// const myUrl = "52.78.180.143"+':'+Config.SVR_PORT;
// const myUrl = myHost + ':' + Config.SVR_PORT;
const myUrl = Config.SVR_LIST;
var port = Config.SVR_PORT;

log.debug("------------------------------------------");
log.debug(' *   my ip : ' + localIp);
log.debug(' * my host : ' + myHost);
log.debug(' *  my url : ' + myUrl);
log.debug("------------------------------------------");

// 1. express 객체 생성
const app = express();
app.set('port', port);
app.use(express.static(__dirname + '/public'));

var privateKey = fs.readFileSync(Config.CERT_PATH_KEY).toString();
var certificate = fs.readFileSync(Config.CERT_PATH).toString();
var ca = fs.readFileSync(Config.CERT_PATH_CA).toString();

var credentials = {key: privateKey, cert: certificate, ca: ca};

const HTTPServer = https.createServer(credentials, app).listen(port, function () {
    console.log('spass proxy server listening on port ' + app.get('port'));
});

const wsModule = require('ws');
// 2. WebSocket 서버 생성/구동
const wss = new wsModule.Server(
    { server: HTTPServer,
        // WebSocket서버에 연결할 HTTP서버를 지정한다.
    }
);

wss.on('connection', (ws, req)=> {
    const ip = req.headers['x-forwarded-for'] || req.connection.remoteAddress;
    log.debug('['+ip+'] ws connected ' + ws);

    const myWs = ws;
    ws.on('message', (message)=> {
        log.debug('['+ip+'] received: ' + message);

    if (message == null || message == undefined || message == "undefined") {
        return;
    }

    const jsonObj = JSON.parse(message);

    if (jsonObj.type && jsonObj.type === TYPE_LOG) {
        log.debug('['+ip+']['+jsonObj.roomId+'][' +jsonObj.userId+'] : ' + jsonObj.text);
        return;
    } else if (jsonObj.type && jsonObj.type === TYPE_INIT) {
        log.debug('['+ip+'] myInfo : userId = ' + jsonObj.userId + ', roomId = ' + jsonObj.roomId);

        const callBackMembers = function (memberList) {
            let allMemberList = memberList;
            allMemberList = allMemberList.concat(wss.findMember(jsonObj.roomId));
            log.debug('['+ip+'] INIT : ALL-USER : ' + JSON.stringify(allMemberList));

            let isBeforUser = false;
            for (let idx = 0; idx < allMemberList.length; idx++) {
                log.debug(">>>>>>>>>>>>>>> allMemberList : " + idx + " - " + allMemberList[idx]);
                if (allMemberList[idx] === jsonObj.userId) {
                    /*
                    log.debug(">>>> 중복 사용자 !!!!!!");
                    myWs.userId = "중복사용자 : "+jsonObj.userId;
                    myWs.roomId = jsonObj.roomId;

                    // 이미 접속 된 사용자
                    let data = {
                        'msg' : '다른 기기 또는 다른 채널을 통해 이미 접속된 사용자입니다.'
                    };
                    myWs.send(generateMsg(TYPE_INIT_FAIL, data, myWs));
                    myWs.userId = "";
                    myWs.roomId = "";
                    return;
                    */
                    // 기존 연결정보에 종료 메시지 전달.
                    const errObj = {
                        'type' : TYPE_INIT_FAIL,
                        'datatype' : 'json',
                        'data' : {'msg' : '다른 기기 또는 다른 채널을 통해 접속되었습니다.'},
                        'userId' : jsonObj.userId,
                        'roomId' : jsonObj.roomId,
                        'desUserId' : jsonObj.userId
                    };
                    let errMsg = JSON.stringify(errObj);
                    wss.broadcast(errMsg, jsonObj.roomId, jsonObj.userId);

                    // 다른 서버에도 메시지를 전송한다. (이중화 시 대비)
                    wss.otherSvrMsg(errMsg);
                    isBeforUser = true;
                }
            }

            myWs.userId = jsonObj.userId;
            myWs.roomId = jsonObj.roomId;

            log.debug(">>>>>>>>>>>>>>> isBeforUser : " + isBeforUser);
            if (!isBeforUser) {
                log.debug(">>>>>>>>>>>>>>> isBeforUser : 셋하러 드러옴 : " + jsonObj.userId);
                allMemberList[allMemberList.length] = jsonObj.userId;
            }

            let data = {
                'connectedUsers' : allMemberList
            };

            if (myWs.readyState === WebSocket.OPEN) {
                myWs.send(generateMsg(TYPE_INIT, data, myWs));
                const sendData = generateMsg(TYPE_CONN, {"userId":myWs.userId}, myWs);
                wss.broadcast(sendData, myWs.roomId);
                log.debug(">>> connectedList : " + JSON.stringify(allMemberList));

                // 다른 서버에도 메시지를 전송한다. (이중화 시 대비)
                wss.otherSvrMsg(sendData);
            }
        };
        wss.otherSvrFindMember(jsonObj.roomId, callBackMembers);

        const inParam = {
            movieRoomId : myWs.roomId,
            userId: myWs.userId,
            type : "in"
        };

        if (inParam.movieRoomId != "" && inParam.userId != "") {
            //SWHAN requestWAS(Config.WAS_URL_INOUT, inParam, function(data){});
        }
    } else if (jsonObj.type && jsonObj.type === TYPE_PING) {
        // PING 신호는 무시!!
    } else if (jsonObj.type && jsonObj.type === TYPE_SVR_COM_MSG) {
        // 서버간 데이터 통신 요청
        const subJsonData = JSON.parse(jsonObj.data);
        wss.broadcast(jsonObj.data, subJsonData.roomId, subJsonData.desUserId);
        myWs.send(generateMsg(TYPE_SVR_COM_OK, "{}", myWs));
    } else if (jsonObj.type && jsonObj.type === TYPE_SVR_FND_MEM) {
        const userList = wss.findMember(jsonObj.roomId);
        const sendData = {
            'type' : TYPE_SVR_FND_MEM_OK,
            'data' : userList
        };
        myWs.send(JSON.stringify(sendData));
    } else {
        // 일단 모든 메시지는 브로드캐스트함
        wss.broadcast(message, myWs.roomId, jsonObj.desUserId);

        // 다른 서버에도 메시지를 전송한다. (이중화 시 대비)
        wss.otherSvrMsg(message);
    }
});

ws.on('error', (error)=> {
    log.error('['+ip+'] ws error : ' + error + ' :: ' + myWs.roomId + ' - ' + myWs.userId);
});

ws.on('close', ()=> {
    // 소켓이 끊어졌을 경우에 호출됨
    log.debug('[' + ip + '] ws close :: ' + myWs.roomId + ' - ' + myWs.userId);
    if (myWs.roomId === undefined || myWs.userId === undefined) {
        return;
    }
    const sendData = generateMsg(TYPE_CLOSE, {"userId":myWs.userId}, myWs);
    wss.broadcast(sendData, myWs.roomId);
    // 다른 서버에도 메시지를 전송한다. (이중화 시 대비)
    wss.otherSvrMsg(sendData);

    // 방에 모든 사용자가 나갔는지 확인
    const funAllMenus = function (mList) {
        let allMemberList = mList;
        allMemberList = allMemberList.concat(wss.findMember(myWs.roomId));

        if (allMemberList.length === 0) {
            const params = {
                //movieRoomId : myWs.roomId,
                req_key : myWs.roomId,
                seq : "2"
                //movieState: "02"
            };

            if (params.movieRoomId && params.movieRoomId !== "") {
                requestWAS(Config.WAS_URL_END, params, function(data){});
            }
        }
    };
    wss.otherSvrFindMember(myWs.roomId, funAllMenus);

    const outParam = {
        movieRoomId : myWs.roomId,
        userId: myWs.userId,
        type : "out"
    };

    if (outParam.movieRoomId && outParam.userId && outParam.movieRoomId !== "" && outParam.userId !== "") {
        //SWHAN requestWAS(Config.WAS_URL_INOUT, outParam, function(data){});
    }});
});

wss.otherSvrFindMember = function(roomId, callBack) {
    // 다른 서버에도 메시지를 전송한다. (이중화 시 대비)
    const svrList = getSvrList();

    if (svrList === null || svrList === "") {
        callBack([]);
        return;
    }

    const urlList = svrList.split(",");
    const sendData = {
        'type' : TYPE_SVR_FND_MEM,
        'roomId' : roomId
    };
    let userList = [];
    let closeCnt = 0;

    const execCallBack = function() {
        closeCnt++;
        if (urlList.length === closeCnt) {
            // 통신이 모두 끝남!!
            callBack(userList);
        }
    };

    urlList.forEach(function(strUrl) {
        const url = strUrl.trim();
        if (!url.endsWith(myUrl)) {
            log.debug('[OTHER_SVR] 다른 서버 연결 : ' + url + ' :: find member!! : ' + roomId);

            try {
                const opts = {rejectUnauthorized: false};
                const otherServer = new WebSocket(url, opts);
                otherServer.on('open', ()=> {
                    otherServer.send(JSON.stringify(sendData));
            });

                otherServer.on('error', (error)=> {
                    log.error('[OTHER_FMEM]['+url+'] ws error !!!! '+error);
            });

                otherServer.on('message', (message)=> {
                    log.debug('[OTHER_FMEM]['+url+'] ws message : '+message);
                const jsonObj = JSON.parse(message);
                if (jsonObj.type && jsonObj.type === TYPE_SVR_FND_MEM_OK) {
                    if (jsonObj.data.length > 0) {
                        userList = userList.concat(jsonObj.data);
                    }
                    try {otherServer.close();} catch (e) {}
                }
            });

                otherServer.on('close', ()=> {
                    // 소켓이 끊어졌을 경우에 호출됨
                    log.debug('[OTHER_FMEM]['+url+'] ws close :: !!');
                execCallBack();

            });
            } catch (e) {
                // 연동 오류
                log.error('[OTHER_FMEM]['+url+'] 연동 실패 : '+e);
                execCallBack();
            }
        } else {
            execCallBack();
        }
    });
};

wss.otherSvrMsg = function (message) {
    // 다른 서버에도 메시지를 전송한다. (이중화 시 대비)
    const svrList = getSvrList();
    if (svrList === null || svrList === "") {
        return;
    }

    const urlList = svrList.split(",");
    const sendData = {
        'type' : TYPE_SVR_COM_MSG,
        'data' : message
    };

    urlList.forEach(function(strUrl) {
        const url = strUrl.trim();
        if (!url.endsWith(myUrl)) {
            log.debug('[OTHER_SVR] 다른 서버 연결 : '+url+' :: message : '+message);
            try {
                const opts = {rejectUnauthorized: false};
                const otherServer = new WebSocket(url, opts);
                otherServer.on('open', ()=> {
                    otherServer.send(JSON.stringify(sendData));
            });

                otherServer.on('error', (error)=> {
                    log.error('[OTHER_SVR]['+url+'] ws error !!!!');
            });

                otherServer.on('message', (message)=> {
                    log.debug('[OTHER_SVR]['+url+'] ws message : ' + message);
                const jsonObj = JSON.parse(message);
                if (jsonObj.type && jsonObj.type === TYPE_SVR_COM_OK) {
                    otherServer.close();
                }
            });

                otherServer.on('close', ()=> {
                    // 소켓이 끊어졌을 경우에 호출됨
                    log.debug('[OTHER_SVR]['+url+'] ws close :: !!');
            });
            } catch (e) {
                // 연동 오류
                log.error('[OTHER_SVR]['+url+'] 연동 실패 : '+e);
            }
        }
    });
};

wss.findMember = function (roomId) {
    let userList = [];
    this.clients.forEach(function(client) {
        log.debug(">>>>> findMember : " + client.roomId + ",  userId : " + client.userId + ", socket STATE["+WebSocket.OPEN+"] : " + client.readyState);
        if (client.roomId === roomId && client.readyState === WebSocket.OPEN && (client.userId !== null && client.userId !== undefined)) {
            userList[userList.length] = client.userId;
        }
    });

    return userList;
};

wss.broadcast = function (data, roomId, desUserId) {
    const jsonData = JSON.parse(data);
    const isCloseSocket = jsonData.type === TYPE_INIT_FAIL;

    this.clients.forEach(function (client) {
        if (client.roomId === roomId) {
            if (client.readyState === WebSocket.OPEN) {
                if (desUserId !== undefined && desUserId != null && desUserId !== "") {
                    // 특정인에게만 전송
                    if (desUserId === client.userId) {
                        log.debug('broadcast SEND ('+desUserId+') : '+client.roomId+' : '+client.userId+' :: '+data);
                        client.send(data);

                        // 종료 패킷이 오면 강제 종료 시킴!
                        if (isCloseSocket) {
                            client.roomId = null;
                            client.userId = null;
                            try {client.close();} catch (e) {}
                        }
                    }
                } else {
                    // 전부 발송
                    if (client.userId !== null && client.userId !== undefined) {
                        log.debug('broadcast SEND ALL : '+client.roomId+' : '+client.userId+' :: '+data);
                        client.send(data);
                    }
                }
            }
        }
    });
};

/****************************************************************
 * 공통 함수 구현 부
 ****************************************************************/
/**
 * 상담사 또는 고객에게 보내는 메시지
 * @data - 전송 데이터
 * @myWs - 웹소켓
 */
function generateMsg(type, data, myWs) {
    let userId = "";
    let roomId = "";
    if (myWs) {
        userId = myWs.userId;
        roomId = myWs.roomId;
    }

    const msg = {
        'type' : type,
        'datatype' : 'json',
        'data' : data,
        'userId' : userId,
        'roomId' : roomId
    };

    return JSON.stringify(msg);
}

/**
 * 이중화 시 서버목록 정보 가져옴
 */
function getSvrList() {
    if (Config.DEV_HOST === myHost) {
        return Config.SVR_DEV_LIST;
    } else {
        return Config.SVR_LIST;
    }
}

/**
 * 와스 IP 가져오기
 * @returns
 */
function getWasIP() {
    if (Config.DEV_HOST === myHost) {
        return Config.WAS_DEV_IP;
    } else {
        return Config.WAS_IP;
    }
}

/****************************************************************
 * WAS 연동 관련
 ****************************************************************/
/**
 * WAS에 데이터를 요청한다.
 * @returns
 */
function requestWAS(url, params, callBack) {
    if (Config.WAS_YN === "N") return;

    let wasIP = getWasIP();
    let wasPORT= Config.WAS_PORT;
    log.debug('[WAS] - IP : ' + wasIP + ", PORT : " + wasPORT);

    const opt = {
        host : wasIP,
        port : wasPORT,
        path : url,
        method : 'post',
        headers : {
            'Content-Type' : 'application/json',
            'Content-Length' : Buffer.byteLength(JSON.stringify(params), 'utf8')
        }
    };

    log.debug('[WAS]['+url+'] PARAMS : ' + JSON.stringify(params));

    const post = httpsClient.request(opt, (res) => {
        res.setEncoding('utf-8');
    res.on('data', (data)=>{
        log.debug('[WAS]['+url+'] REC-DATA : ' + data);
    callBack(data);
});

    res.on('end', ()=>{
        log.debug('[WAS]['+url+'] END !!');
});
});

    post.write(JSON.stringify(params));
    post.end();
}

log.debug('Server running. Visit PORT : ' + Config.SVR_PORT + '\n\n\
Some important notes:\n\
  * Note the HTTPS; there is no HTTP -> HTTPS redirect.\n\
  * You\'ll also need to accept the invalid TLS certificate.\n\
  * Some browsers or OSs may not allow the webcam to be used by multiple pages at once. You may need to use two different browsers or machines.\n');
