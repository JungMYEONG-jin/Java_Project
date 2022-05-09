var dbconn = require('../config/dbconn.js');
var util = require('../util/util.js');

var platformList = null;
var platformMap = null;

function loadPlatform() {
	if (platformList == null || platformMap == null) {
		var query = dbconn('MASTER_PLATFORM')
		.orderBy('IDX', 'ASC')	
		util.logger.info('- QUERY(' + query + ')');
		query.then((rows) => {
			var list = [];
			var map = {};
			rows.forEach(function(row, index){
				var item = {};
				item.idx = row.IDX;
				item.name = row.NAME;
				list.push(item);
				var key = String(item.idx);
				map[key] = item;
			});
			platformList = list;
			platformMap = map;
		}).catch((err) => {
			util.logger.error('- ERROR(' + err + ')');
		});
	}
}

function getName(idx) {
	var key = String(idx);
	return platformMap[idx];
}

function getList() {
	return platformList;
}

module.exports = {
		load: loadPlatform,
		name: getName,
		list: getList
};
