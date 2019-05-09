//require(['jquery'], function () { // 모듈 호출 이후, 콜백 함수 작성 });

function json2array(json){
    var result = [];
    var keys = Object.keys(json);
    keys.forEach(function(key){
        result.push(json[key]);
    });
    return result;
}

$(document).ready(function () {
    //var jQuery = $;
    //alert($().jquery);
    console.log("list");
    $.ajax({
        url: '/caradverts',
        dataType: 'json',
        success: function (data) {
            //$('#time').append(data);

        /*<tr>
            <th scope="row">1</th>
                <td>@carAdvert.title</td>
            <td>@carAdvert.fuel</td>
            <td>@carAdvert.price</td>
            <td>@{carAdvert.newThing match {
            case true => "Yes"
            case false => "No"
            }}</td>
            <td>@carAdvert.mileage</td>
            <td>@carAdvert.firstRegistration</td>
            <td><button type="button" class="btn btn-outline-info btn-sm" data-toggle="modal" data-target="#editCarAdvertModal" data-whatever="@@mdo">
                Edit</button></td>
            </tr>*/

            var str = '';

            //console.log(JSON.stringify(data));

            var a = json2array(data)
            //console.log(a.toString)
           // a.forEach(caradvert, index)
            for (var i=0; i< data.length; i++){
                console.log(data[i].title)
                /*var myArr = [];
                myArr.push();
                myArr.push(item.data[i].name);
                myArr.push(item.data[i].sum);
                item.data[i] = myArr;*/
            }


           /* $.each(a, function(c){
                console.log(c.title);
            })*/

     /*       $.each(data, function(index, entry) {

                console.log(index + entry["name"])
                console.log(entry["name"])

            });*/

       /*     for (var name in data) {
                str += '<td>' + data[name] + '</td>';
            }*/
            //$('#caradverts-tbody').
            //$('#timezones').html('<ul>' + str + '</ul>');

          //  console.log(str)

        }
    })

});
//});
