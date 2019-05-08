requirejs.config({
    baseUrl: 'assets/lib', // 'js' 라는 폴더를 기본 폴더로 설정한다.

    paths:{
        'jquery':'jquery/jquery.min',
        'bootstrap': 'bootstrap/js/bootstrap.bundle.min'
    },
    shim:{
        'bootstrap': {
            deps: ['jquery']
        }
    }
});

requirejs( [
        'jquery',
        'bootstrap'
    ],

    function ($,bootstrap) {
        $(document).ready(function () {
            //var jQuery = $;
            //alert($().jquery);
            console.log("Welcome to Simple Car Adverts's JavaScript!");


            $('#createCarAdvertModal').on('show.bs.modal', function (event) {
             //   var button = $(event.relatedTarget) // Button that triggered the modal
             //   var recipient = button.data('whatever') // Extract info from data-* attributes
                // If necessary, you could initiate an AJAX request here (and then do the updating in a callback).
                // Update the modal's content. We'll use jQuery here, but you could use a data binding library or other methods instead.
                var modal = $(this)
             //   modal.find('.modal-title').text('New message to ' + recipient)
             //   modal.find('.modal-body input').val(recipient)
            })

            $('#editCarAdvertModal').on('show.bs.modal', function (event) {
                var button = $(event.relatedTarget) // Button that triggered the modal
                var recipient = button.data('whatever') // Extract info from data-* attributes
                // If necessary, you could initiate an AJAX request here (and then do the updating in a callback).
                // Update the modal's content. We'll use jQuery here, but you could use a data binding library or other methods instead.
                var modal = $(this)
                modal.find('.modal-title').text('New message to ' + recipient)
                modal.find('.modal-body input').val(recipient)
            })




        });
    }
);

