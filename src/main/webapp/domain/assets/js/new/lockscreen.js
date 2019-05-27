/**
 * Created by dynamicniu on 2018/4/6.
 */
function lsLogin() {
    var url = getRootPath() + '/loginAction/signin';
    var params = {
        'loginName': 'NIUBEN',
        'password': '123456'
    }

    alert(params.password + ' ' + url)

       $.ajax({
           type: "POST",
           url: url,
           async: false,
           data: params,
//            data: data.serialize(),
//            dataType: 'json',
           error: function (request) {

           },
           success: function (result) {
               if (result.resultCode == 'success') {
                   alert("Error");
               }
           }
       });
}