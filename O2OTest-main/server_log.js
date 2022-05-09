/**
 *  * Signaling Server Log
 *   *
 *    * author : ShinhanDS (kswksk)
 *     * date : 2018.09.04
 *      */

const Config = require('./server_cfg');
const winston = require('winston');
const winstonDaily = require('winston-daily-rotate-file');
const moment = require('moment');
 
function timeStampFormat() {
    return moment().format('YYYY-MM-DD HH:mm:ss.SSS ZZ');                            
};
//logger 설정
const logger = new (winston.Logger)({
    transports: [
        new (winstonDaily)({
            name: 'info-file',
            filename: Config.LOG_DIR+'/signaling',
            datePattern: '_yyyy-MM-dd.log',
            colorize: false,
            maxsize: 50000000,
            maxFiles: 1000,
            level: Config.LOG_LEVEL,
            showLevel: true,
            json: false,
            timestamp: timeStampFormat
        }),
        new (winston.transports.Console)({
            name: 'debug-console',
            colorize: true,
            level: Config.LOG_LEVEL,
            showLevel: true,
            json: false,
            timestamp: timeStampFormat
        })
    ],
    exceptionHandlers: [
        new (winstonDaily)({
            name: 'exception-file',
            filename: Config.LOG_DIR+'/signaling_exception',
            datePattern: '_yyyy-MM-dd.log',
            colorize: false,
            maxsize: 50000000,
            maxFiles: 1000,
            level: 'error',
            showLevel: true,
            json: false,
            timestamp: timeStampFormat
        }),
        new (winston.transports.Console)({
            name: 'exception-console',
            colorize: true,
            level: Config.LOG_LEVEL,
            showLevel: true,
            json: false,
            timestamp: timeStampFormat
        })
    ]
});
 
//logger를 통한 로그 출력
//logger.info("infolevel 로깅");
//logger.debug("debuglevel 로깅");
logger.debug("-------------------- [LOG INFO] --------------------");
logger.debug(" 111111 LOG PATH  : "+Config.LOG_DIR);
logger.debug(" LOG LEVEL : "+Config.LOG_LEVEL);
logger.debug("----------------------------------------------------");

module.exports = logger;

