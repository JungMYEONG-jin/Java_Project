var error = require('../config/error.js');
var path = require('../config/path.js');
const crypto = require('crypto');

var fs = require('fs');

function makeResult(result, data) {
	var json = {};
	json.result = result;
	json.message = error[result];
	if (!isNull(data)) {
		json.data = data;
	} else {
		json.data = {};
	}

	if (json.data.error_detail_message){
		json.message  += '(' + data.error_detail_message + ')';
	}

	return json;
}

function isNull(value) {
	return (value == null || typeof value === 'undefined' || Number.isNaN(value));
}

function convBoolean(value) {
	if (typeof value === 'string' || value instanceof String) {
		return value == 'true';
	}
	return value;
}

// var logger = {
// 		info : function(msg) {
// 			console.info('[' + formatDate(new Date(), 'yyyy-MM-dd HH:mm:ss.ffff') + "] " + msg);
// 		},
// 		debug : function(msg) {
// 			console.debug('[' + formatDate(new Date(), 'yyyy-MM-dd HH:mm:ss.ffff') + "] " + msg);
// 		},
// 		warn : function(msg) {
// 			console.warn('[' + formatDate(new Date(), 'yyyy-MM-dd HH:mm:ss.ffff') + "] " + msg);
// 		},
// 		error : function(msg) {
// 			console.error('[' + formatDate(new Date(), 'yyyy-MM-dd HH:mm:ss.ffff') + "] " + msg);
// 			console.trace();
// 		}
// }

function formatDate(date, format, utc) {
	var MMMM = ["\x00", "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];
	var MMM = ["\x01", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];
	var dddd = ["\x02", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"];
	var ddd = ["\x03", "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"];

	function ii(i, len) {
		var s = i + "";
		len = len || 2;
		while (s.length < len) s = "0" + s;
		return s;
	}

	var y = utc ? date.getUTCFullYear() : date.getFullYear();
	format = format.replace(/(^|[^\\])yyyy+/g, "$1" + y);
	format = format.replace(/(^|[^\\])yy/g, "$1" + y.toString().substr(2, 2));
	format = format.replace(/(^|[^\\])y/g, "$1" + y);

	var M = (utc ? date.getUTCMonth() : date.getMonth()) + 1;
	format = format.replace(/(^|[^\\])MMMM+/g, "$1" + MMMM[0]);
	format = format.replace(/(^|[^\\])MMM/g, "$1" + MMM[0]);
	format = format.replace(/(^|[^\\])MM/g, "$1" + ii(M));
	format = format.replace(/(^|[^\\])M/g, "$1" + M);

	var d = utc ? date.getUTCDate() : date.getDate();
	format = format.replace(/(^|[^\\])dddd+/g, "$1" + dddd[0]);
	format = format.replace(/(^|[^\\])ddd/g, "$1" + ddd[0]);
	format = format.replace(/(^|[^\\])dd/g, "$1" + ii(d));
	format = format.replace(/(^|[^\\])d/g, "$1" + d);

	var H = utc ? date.getUTCHours() : date.getHours();
	format = format.replace(/(^|[^\\])HH+/g, "$1" + ii(H));
	format = format.replace(/(^|[^\\])H/g, "$1" + H);

	var h = H > 12 ? H - 12 : H == 0 ? 12 : H;
	format = format.replace(/(^|[^\\])hh+/g, "$1" + ii(h));
	format = format.replace(/(^|[^\\])h/g, "$1" + h);

	var m = utc ? date.getUTCMinutes() : date.getMinutes();
	format = format.replace(/(^|[^\\])mm+/g, "$1" + ii(m));
	format = format.replace(/(^|[^\\])m/g, "$1" + m);

	var s = utc ? date.getUTCSeconds() : date.getSeconds();
	format = format.replace(/(^|[^\\])ss+/g, "$1" + ii(s));
	format = format.replace(/(^|[^\\])s/g, "$1" + s);

	var f = utc ? date.getUTCMilliseconds() : date.getMilliseconds();
	format = format.replace(/(^|[^\\])fff+/g, "$1" + ii(f, 3));
	f = Math.round(f / 10);
	format = format.replace(/(^|[^\\])ff/g, "$1" + ii(f));
	f = Math.round(f / 10);
	format = format.replace(/(^|[^\\])f/g, "$1" + f);

	var T = H < 12 ? "AM" : "PM";
	format = format.replace(/(^|[^\\])TT+/g, "$1" + T);
	format = format.replace(/(^|[^\\])T/g, "$1" + T.charAt(0));

	var t = T.toLowerCase();
	format = format.replace(/(^|[^\\])tt+/g, "$1" + t);
	format = format.replace(/(^|[^\\])t/g, "$1" + t.charAt(0));

	var tz = -date.getTimezoneOffset();
	var K = utc || !tz ? "Z" : tz > 0 ? "+" : "-";
	if (!utc) {
		tz = Math.abs(tz);
		var tzHrs = Math.floor(tz / 60);
		var tzMin = tz % 60;
		K += ii(tzHrs) + ":" + ii(tzMin);
	}
	format = format.replace(/(^|[^\\])K/g, "$1" + K);

	var day = (utc ? date.getUTCDay() : date.getDay()) + 1;
	format = format.replace(new RegExp(dddd[0], "g"), dddd[day]);
	format = format.replace(new RegExp(ddd[0], "g"), ddd[day]);

	format = format.replace(new RegExp(MMMM[0], "g"), MMMM[M]);
	format = format.replace(new RegExp(MMM[0], "g"), MMM[M]);

	format = format.replace(/\\(.)/g, "$1");

	return format;
};

function initPassword() {
	return 'DB0547DD49C5ADCA6CBDA03522A88C5D243BBDDB42F19441F4D928866449AD25';
}

function zeroPad(nr, base) {
	var len = (String(base).length - String(nr).length) + 1;
	return len > 0 ? new Array(len).join('0') + nr : nr;
}

function getFileExtension(filename){
	return filename.split('.').pop();
}

function checkFileExtension(filename, extensions){
	var target = getFileExtension(filename);
	for (var i =0; i < extensions.length; i++){
		if (target == extensions[i])
			return true;
	}

	return false;
}


function requireCacheReload(reloafedFilePath, obj) {

	var path_regex = reloafedFilePath.substring(reloafedFilePath.indexOf('/')).replace(/\//gi, "(\\\\|\\/)");
	var regex = new RegExp('^'+path_regex);

	var cache_id = Object.keys(require.cache).filter(x => {
		var filterNameSubStr = x.substr(process.cwd().length);
		return regex.test(filterNameSubStr);
	});

	if (cache_id.length == 1) {
		delete require.cache[cache_id];
		return require(reloafedFilePath);
	} else {
		return obj;
	}
}

function getUseTypeName(useType) {
	var arrUseType = {'A':'슈퍼관리자', 'M':'관리자', 'D':'개발자', 'T':'테스터'};
	return isNull(arrUseType[useType]) ? "" : arrUseType[useType];
}

function aes256_cipher(word) {
	// const cipher = crypto.createCipher('aes-256-cbc', path.enc_key);
	// let result = cipher.update(word, 'utf8', 'base64');
	// result += cipher.final('base64');
	// return result;

	let iv = crypto.randomBytes(16);
	let cipher = crypto.createCipheriv('aes-256-cbc', Buffer.from(path.enc_key, 'hex'), iv);
	let encrypted = cipher.update(word);
	encrypted = Buffer.concat([encrypted, cipher.final()]);
	var enc = iv.toString('hex') + ':' + encrypted.toString('hex');

	return enc;

}

function aes256_decipher(word) {
	// const decipher = crypto.createDecipher('aes-256-cbc', path.enc_key);
	// let result = decipher.update(word, 'base64', 'utf8');
	// result += decipher.final('utf8');
	// return result;

	let textParts = word.split(':');
	let iv = Buffer.from(textParts.shift(), 'hex');
	let encryptedText = Buffer.from(textParts.join(':'), 'hex');
	let decipher = crypto.createDecipheriv('aes-256-cbc', Buffer.from(path.enc_key, 'hex'), iv);
	let decrypted = decipher.update(encryptedText);
	decrypted = Buffer.concat([decrypted, decipher.final()]);
	return decrypted.toString();
}

function cloneObject(obj) {
	return JSON.parse(JSON.stringify(obj));
}

/**
 *
 */
function makeScherdulerResultName(project_name, execute_time ){
	return project_name +"_"+ formatDate(new Date(execute_time), "yyyyMMdd_HHmmss") + "_result";
}

function makeBuildTableMeta(row){
	file_data = {};
	meta = {};
	meta.project = {};
	meta.project.idx = row.IDX_PROJECT;
	meta.project.name = row.NAME_PROJECT;
	meta.project.app_id = row.APP_ID;
	meta.target = {};
	meta.target.idx = row.IDX_TARGET;
	meta.target.name = row.NAME_TARGET;
	meta.agent = {};
	meta.agent.idx = row.IDX_AGENT;
	meta.agent.name = row.NAME_AGENT;
	meta.agent.address = row.ADDRESS_AGENT;
	meta.platform = {};
	meta.platform.idx = row.IDX_PLATFORM;
	meta.platform.name = row.NAME_PLATFORM;
	meta.build_type = {};
	meta.build_type.idx = row.IDX_BUILD_TYPE;
	meta.build_type.name = row.NAME_BUILD_TYPE;
	meta.build_type.idx_cert_type = row.IDX_CERT_TYPE;
	meta.build_type.idx_build_type = row.IDX_BUILD_TYPE2;
	meta.build_type.code_name = row.CODE_NAME;
	meta.deploy_type = {};
	meta.deploy_type.idx = row.IDX_DEPLOY_TYPE;
	meta.deploy_type.name = row.NAME_DEPLOY_TYPE;
	meta.cert = {};
	meta.cert.idx = row.IDX_CERT;
	meta.cert.name = row.NAME_CERT;
	meta.cert.filename = row.FILENAME_CERT;
	meta.cert.path = row.PATH_CERT;
	try{
		meta.cert.password = aes256_decipher(row.PASSWORD_CERT);
	} catch (err) {
		meta.cert.password = "";
	}


	file_data.cert = row.DATA_CERT;
	meta.provision = {};
	meta.provision.idx = null;
	meta.provision.name = null;
	meta.provision.filename = null;
	meta.provision.path = null;
	meta.keystore = {};
	meta.keystore.idx = row.IDX_KEYSTORE;
	meta.keystore.name = row.NAME_KEYSTORE;
	meta.keystore.filename = row.FILENAME_KEYSTORE;
	meta.keystore.path = row.PATH_KEYSTORE;
	try{
		meta.keystore.password = aes256_decipher(row.PASSWORD_KEYSTORE);
	} catch (err) {
		meta.keystore.password = "";
	}

	meta.keystore.alias = row.ALIAS_KEYSTORE;

	try{
		meta.keystore.alias_password = aes256_decipher(row.ALIAS_PASSWORD_KEYSTORE);
	} catch (err) {
		meta.keystore.alias_password = "";
	}


	meta.keystore.is_signing = row.IS_SIGNING == "Y" ? "true" : "false";
	file_data.keystore = row.DATA_KEYSTORE;
	meta.repository = {};
	meta.repository.idx = row.IDX_REPOSITORY;
	meta.repository.name = row.NAME_REPOSITORY;
	meta.repository.username = row.CONNECTION_ID_REPOSITORY;
	meta.repository.password = row.PASSWORD_REPOSITORY;
	meta.repository.base = row.ADDRESS_REPOSITORY
	meta.repository.main = row.REPOSITORY_MAIN;
	meta.repository.paths = [];
	meta.etc = {};
	meta.etc.memo = row.MEMO;
	meta.etc.version = row.VERSION;
	meta.etc.variant = row.VARIANT;
	meta.etc.repoappfolder = row.REPOSITORY_APP_FOLDER;
	meta.etc.scheme = row.SCHEME;
	meta.etc.option = row.OPTION;
	meta.etc.extension = (row.EXTENSION == null) ? "" : row.EXTENSION;
	meta.etc.zip_password = (row.ZIP_PASSWORD == null) ? "" : row.ZIP_PASSWORD;
	meta.etc.download_path = (row.DOWNLOAD_PATH == null) ? "" : row.DOWNLOAD_PATH;
	meta.etc.xcode_version = (row.XCODE_VERSION == null) ? "" : row.XCODE_VERSION;
	meta.etc.arxan_yn = (row.ARXAN_YN == null) ? "" : row.ARXAN_YN;
	meta.etc.arxan_command = (row.ARXAN_YN == null) ? "" : row.ARXAN_COMMAND;
	meta.project_build_type_idx = row.PROJECT_BUILD_TYPE_IDX;
	meta.file_data = file_data;

	try{
		meta.etc.fortify_yn = build_info.SCHEDULE_YN;
	}catch (err) {
		// 만약 익셉션난다면 n이 디폴트값
		meta.etc.fortify_yn = 'N';
	}
	console.log("%%%%%%%%%%%" + JSON.stringify(meta))
	return meta;
}

function createWriteStreamSync(file, options) {
	if (!options)
		options = {};
	if (!options.flags)
		options.flags = 'w';
	if (!options.fd)
		options.fd = fs.openSync(file, options.flags);
	return fs.createWriteStream(null, options);
}

function createReadStreamSync(file, options) {
	if (!options)
		options = {};
	if (!options.flags)
		options.flags = 'r';
	if (!options.fd)
		options.fd = fs.openSync(file, options.flags);
	return fs.createReadStream(null, options);
}

module.exports = {
		makeResult: makeResult,
		makeScherdulerResultName: makeScherdulerResultName,
		formatDate: formatDate,
		isNull: isNull,
		convBoolean: convBoolean,
		initPassword: initPassword,
		zeroPad : zeroPad,
		getFileExtension : getFileExtension,
		checkFileExtension : checkFileExtension,
		requireCacheReload : requireCacheReload,
		getUseTypeName : getUseTypeName,
		aes256_cipher : aes256_cipher,
		aes256_decipher : aes256_decipher,
		cloneObject : cloneObject,
		makeMetaFromBuildTable: makeBuildTableMeta,
		createReadStreamSync:createReadStreamSync,
		createWriteStreamSync:createWriteStreamSync,
};
