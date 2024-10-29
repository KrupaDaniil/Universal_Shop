document.addEventListener("DOMContentLoaded", (e) => {
    let p_Name = document.getElementById("userName");
    let p_Surname = document.getElementById("userSurname");
    let p_Phone = document.getElementById("userPhone");
    let p_Email = document.getElementById("userEmail");
    let p_Password = document.getElementById("userPassword");


    p_Phone.addEventListener("keypress", (e) => {
        if (!/\d/.test(e.key)) {
            e.preventDefault();
        }
    })

    let regex_1 = new RegExp(/^[a-zA-Zа-яА-ЯІіЇї']*$/);
    let regex_2 = new RegExp(/^\w+@\w+\.[a-zA-Z]+(\.[a-zA-Z]+)?$/);

    p_Name.addEventListener("input", (e) => {
        testRegex(regex_1, p_Name)
    });

    function testRegex(regex, param) {
        if (!regex.test(param.value)) {
            param.classList.add("invalidData");
        }
        else {
            param.classList.remove("invalidData");
        }
    }
});