document.addEventListener("DOMContentLoaded", function(){
    var changeFieldState = function(isChecked){
        if(isChecked){
            document.getElementById('mileage').setAttribute("disabled", "disabled");
            document.getElementById('firstRegistration').setAttribute("disabled", "disabled");
        }else{
            document.getElementById('mileage').removeAttribute("disabled");
            document.getElementById('firstRegistration').removeAttribute("disabled");
        }
    }

    var newThing = document.getElementById('newThing');
    if(newThing != null){
        newThing.addEventListener("change", function(){
            var isChecked2 = this.checked;
            this.value=isChecked2;
            changeFieldState(isChecked2);
            /*document.getElementById('mileage').setAttribute("disabled",isChecked2);
            document.getElementById('firstRegistration').setAttribute("disabled",isChecked2);*/
        });

        var isChecked1 = newThing.value =="true";
        if(isChecked1){
            newThing.setAttribute('checked','checked');
        }else{
            newThing.removeAttribute('checked');
        }
        changeFieldState(isChecked1);
    }
});