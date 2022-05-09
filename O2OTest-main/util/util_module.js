// var logger = require('../config/logger.js');
// var path = require('../config/path.js');
// const crypto = require('crypto');
var fs = require('fs');

function makeDictionaryJsFile(dictionary,filePath){

	try{
		if(dictionary == undefined){
			dictionary = {};
		}

		var datas = fs.readFileSync(filePath, 'utf8');

		var startFunc = false;

		var tmpData = [];

		for (let data of datas.split("\n")){
			if(data.indexOf("\//") != -1){
				// 주석 행은 생략 한다.
				continue;
			}

			// String 문구에서 function을 찾는다.
			// function이라는 Stirng이 있다면 function의
			// 시작으로 보고 값 추출을 시작한다.
			if(data.indexOf("function")!= -1){
				startFunc = true;
			}

			if(startFunc){
				tmpData.push(data);

				if(balancedBraces(tmpData.join(''))){
					startFunc = false;

					var key = getFunctionNameAsString(tmpData.join(''));
					// util.logger.info("function Key : " + key) ;
					var valueFunc = tmpData.slice(1, tmpData.length - 1);
					// util.logger.info("function Contents : " + valueFunc) ;
					var value = valueFunc.join('');
					// console.log("value : " + value);

					if(dictionary.hasOwnProperty(key)){
						delete dictionary[key]
					}
					dictionary[key] = value;

					tmpData = [];
				}
			}
		}

		return dictionary;
	}catch (err) {
		console.log("load err: " + err);
	}
}

/**
 *
 * 중괄호를 찾는다.
 * function 정의시 fuction의 중괄호 시작과({) 끝부분이(}) 일치 했을경우 (중괄호 시작 (열림) 중괄호 끝 (닫힘)
 * true를 리턴.
 * 중괄호가 시작만하고 닫히지 않았으면 false를 리텅
 *
 * */
function balancedBraces(inputStr) {

	try {
		var checkStr = "[]{}()";
		var stack = [];
		var i, character, bracePosition;

		for (i = 0; character = inputStr[i]; i++) {
			bracePosition = checkStr.indexOf(character);

			if (bracePosition === -1) {
				continue;
			}

			if (bracePosition % 2 === 0) {
				stack.push(bracePosition + 1);
			} else if (stack.pop() !== bracePosition) {
				return 'NO';
			}
		}

		return stack.length === 0;

	} catch (err) {
		return false;
	}
}

// 처음이랑 마지막만 뺴고 입력
function getFunctionNameAsString (funcDefAsstr){

	const funcInxStr = "function ";
	let funcStrIdx = funcDefAsstr.indexOf(funcInxStr) + funcInxStr.length;
	var tmp = funcDefAsstr.substring(funcStrIdx);

	let funcStartIdx = tmp.indexOf('(');

	return tmp.substring( 0, funcStartIdx);
};

function deleteFolder(deleteFolder) {
	try {
		if (fs.existsSync(deleteFolder)) {
			var files = fs.readdirSync(deleteFolder);
			files.forEach(element => {
				fs.unlinkSync(deleteFolder + "/" + element);
			});
			fs.rmdirSync(deleteFolder);

			console.log("Recursive: Directories Deleted!");

		}

	} catch (err) {
		console.log("Recursive: Directories Deleted! Err " + err);
		return false;
	}

	return true;
}

function deletefile(files) {
	try {
		if (fs.existsSync(files)) {
			fs.unlinkSync(files);
			console.log("Recursive: Directories Deleted!");
		}

	} catch (err) {
		console.log("Recursive: Directories Deleted! Err " + err);
		return false;
	}

	return true;
}

function copySrcToDstDir(src, dst) {
	try {
		fs.mkdirSync(dst);

		var files = fs.readdirSync(src);

		files.forEach(element => {
			if (element != '.DS_Store') {
				fs.copyFileSync(src + '/' + element, dst + '/' + element);
			}
		});
		util.logger.info("File Copy Succuss!");

		//원본 폴더 삭제
		files.forEach(element => {
			if (element != '.DS_Store') {
				fs.unlinkSync(src + '/' + element);
			}
		});

		fs.rmdirSync(src);
		util.logger.info("Src Dir Delete Successs");

	} catch (err) {
		return false;
	}

	return true;
}

module.exports = {
	makeDictionaryJsFile: makeDictionaryJsFile,
	getFunctionNameAsString:getFunctionNameAsString,
	balancedBraces:balancedBraces,
	deleteFolder:deleteFolder,
	deletefile:deletefile,
	copySrcToDstDir:copySrcToDstDir

};
