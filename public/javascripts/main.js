/*requirejs.config({
    baseUrl: '/assets', // 'js' 라는 폴더를 기본 폴더로 설정한다.

    paths:{
        'jquery':'lib/jquery/jquery.min',
        'bootstrap': 'lib/bootstrap/js/bootstrap.bundle.min'
    },
    shim:{
        'bootstrap': {
            deps: ['jquery']
        },
        jquery : {
            exports: '$'
        }
    }
});

requirejs( [
        'jquery',
        'bootstrap'
    ],

    function ($,bootstrap) {*/
        $(document).ready(function () {
            //var jQuery = $;
            //alert($().jquery);
            console.log("Welcome to Simple Car Adverts's JavaScript!");

            if($('#createCarAdvertModal') != null) {
                $('#createCarAdvertModal').on('show.bs.modal', function (event) {
                    //   var button = $(event.relatedTarget) // Button that triggered the modal
                    //   var recipient = button.data('whatever') // Extract info from data-* attributes
                    // If necessary, you could initiate an AJAX request here (and then do the updating in a callback).
                    // Update the modal's content. We'll use jQuery here, but you could use a data binding library or other methods instead.
                    var modal = $(this)
                    //   modal.find('.modal-title').text('New message to ' + recipient)
                    //   modal.find('.modal-body input').val(recipient)
                })
            }

            if($('#editCarAdvertModal') != null) {
                $('#editCarAdvertModal').on('show.bs.modal', function (event) {
                    var button = $(event.relatedTarget) // Button that triggered the modal
                    var recipient = button.data('whatever') // Extract info from data-* attributes
                    // If necessary, you could initiate an AJAX request here (and then do the updating in a callback).
                    // Update the modal's content. We'll use jQuery here, but you could use a data binding library or other methods instead.
                    var modal = $(this)
                    modal.find('.modal-title').text('New message to ' + recipient)
                    modal.find('.modal-body input').val(recipient)
                })
            }

            if($('#newThing') != null){
                var isChecked1 = $('#newThing').val()=="true";
                if(isChecked1){
                    $('#newThing').attr('checked','checked');
                }else{
                    $('#newThing').removeAttr('checked');
                }
                $('#mileage').attr("disabled",isChecked1);
                $('#firstRegistration').attr("disabled",isChecked1);
                $('#newThing').change(function(){
                    var isChecked2 = this.checked;
                    this.value=isChecked2
                    $('#mileage').attr("disabled",isChecked2);
                    $('#firstRegistration').attr("disabled",isChecked2);
                })
            }
        });
 /*   }
);
*/
