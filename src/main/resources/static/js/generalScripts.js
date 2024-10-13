document.addEventListener("DOMContentLoaded", (e) => {
    let flInput = document.getElementById("userPhone");
    let ps_1 = document.getElementById("userPassword");
    let ps_2 = document.getElementById("confirmPassword");


    flInput.addEventListener("keypress", (e) => {
        if (!/\d/.test(e.key)) {
            e.preventDefault();
        }
    })
});