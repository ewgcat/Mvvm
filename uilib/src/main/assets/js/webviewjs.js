var webviewjs = {};
webviewjs.os = {};
webviewjs.os.isIOS = /iOS|iPhone|iPad|iPod/i.test(navigator.userAgent);
webviewjs.os.isAndroid = !webviewjs.os.isIOS;
webviewjs.callbacks = {}

webviewjs.callback = function (callbackname, response) {
   var callbackobject = webviewjs.callbacks[callbackname];
   console.log("xxxx"+callbackname);
   if (callbackobject !== undefined){
       if(callbackobject.callback != undefined){
          console.log("xxxxxx"+response);
            var ret = callbackobject.callback(response);
           if(ret === false){
               return
           }
           delete webviewjs.callbacks[callbackname];
       }
   }
}

webviewjs.takeNativeAction = function(commandname, parameters){
    console.log("webviewjs takenativeaction")
    var request = {};
    request.name = commandname;
    request.param = parameters;
    if(window.webviewjs.os.isAndroid){
        console.log("android take native action" + JSON.stringify(request));
        window.xiangxuewebview.takeNativeAction(JSON.stringify(request));
    } else {
        window.webkit.messageHandlers.xiangxuewebview.postMessage(JSON.stringify(request))
    }
}

webviewjs.takeNativeActionWithCallback = function(commandname, parameters, callback) {
    var callbackname = "nativetojs_callback_" +  (new Date()).getTime() + "_" + Math.floor(Math.random() * 10000);
    webviewjs.callbacks[callbackname] = {callback:callback};

    var request = {};
    request.name = commandname;
    request.param = parameters;
    request.param.callbackname = callbackname;
    if(window.webviewjs.os.isAndroid){
        window.xiangxuewebview.takeNativeAction(JSON.stringify(request));
    } else {
        window.webkit.messageHandlers.xiangxuewebview.postMessage(JSON.stringify(request))
    }
}

window.webviewjs = webviewjs;
