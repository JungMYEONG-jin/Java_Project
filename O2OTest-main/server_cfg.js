/**
 * Signaling Server Config
 *
 * @auth : ShinhanDS (kswksk)
 * @date : 2018.09.04
 */
// SERVER INFO
exports.DEV_HOST 	    = 'mbmspa1t';
exports.SVR_IP 		    = '0.0.0.0';
exports.SVR_PORT 	    = 8200;

// duplication Server
// ex) 'wss://xxx.xxx.xxx.xxx:8200,wss://xxx.xxx.xxx.xxx:8200';

exports.SVR_LIST        = 'wss://spass.hancy.kr:8200';
exports.SVR_DEV_LIST	= '';

// var rootPAth = '/home/ubuntu/spass/O2OTest/spass';
var rootPAth = '/Users/a60067648/Desktop/poc/O2OTest/spass';

// exports.CERT_PATH       = rootPAth + '/cert/cert1.pem';
// exports.CERT_PATH_KEY   = rootPAth + '/cert/privkey1.pem';
// exports.CERT_PATH_CA    = rootPAth + '/cert/fullchain1.pem';

// SSL CERTasdad
exports.CERT_PATH       = './cert/cert1.pem';
exports.CERT_PATH_KEY   = './cert/privkey1.pem';
exports.CERT_PATH_CA    = './cert/fullchain1.pem';

// LOG INF
// exports.LOG_DIR 	    = rootPAth + '/shblog/s-pass/nodejs';
exports.LOG_DIR 	    = './shblog/s-pass/nodejs';
exports.LOG_LEVEL	    = 'debug';

// CONFIG WAS
exports.WAS_YN			= 'N';
exports.WAS_IP 			= '192.168.60.160';
exports.WAS_DEV_IP		= '172.23.253.91';
exports.WAS_PORT		= 8080;
exports.WAS_URL_END     = '/noface/NF1042.do';

