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
        });
    }
);

