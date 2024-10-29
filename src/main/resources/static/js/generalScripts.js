document.addEventListener("DOMContentLoaded", (e) => {
    let p_Login = document.getElementById("inpLogin");
    let pi_Password = document.getElementById("inpPassword");
    let p_Name = document.getElementById("userName");
    let p_Surname = document.getElementById("userSurname");
    let p_Phone = document.getElementById("userPhone");
    let p_Email = document.getElementById("userEmail");
    let p_Password = document.getElementById("userPassword");

    if (document.contains(p_Phone)) {

        p_Phone.addEventListener("keypress", (e) => {
            if (!/\d/.test(e.key)) {
                e.preventDefault();
            }
        })
    }


    let regex_1 = new RegExp(/^[a-zA-Zа-яА-ЯІіЇї']*$/);
    let regex_2 = new RegExp(/^[\w\W]+@\w+\.[a-zA-Z]+(\.[a-zA-Z]+)?$/);
    let regex_3 = new RegExp(/^(?=.{8,32}$)([A-Z\d_-]{1,})?[a-z\d_-]+([A-Z]{1,})([\w-]+)?$/);

    if (document.contains(p_Name)) {
        p_Name.addEventListener("input", (e) => {
            testRegex(regex_1, p_Name);
        });
    }


    if (document.contains(p_Surname)) {
        p_Surname.addEventListener("input", (e) => {
            testRegex(regex_1, p_Surname);
        });
    }


    if (document.contains(p_Email)) {
        p_Email.addEventListener("input", (e) => {
            testRegex(regex_2, p_Email);
        });
    }

    if (document.contains(p_Password)) {
        p_Password.addEventListener("input", (e) => {
            testRegex(regex_3, p_Password);
        });

        p_Password.addEventListener("focus", (e) => {
            let divInfo = document.createElement("div");
            divInfo.classList.add("d-flex", "justify-content-center", "align-items-center", "my-1");
            divInfo.setAttribute("id", "password-info");
            let textContainer = document.createElement("div");
            textContainer.classList.add("text-info");
            textContainer.innerText = "valid characters: a-z, _ -, A-Z, 0-9";
            divInfo.appendChild(textContainer);
            if (document.contains(document.getElementById("passwordContainer"))) {
                document.getElementById("passwordContainer").appendChild(divInfo);
            }
        });

        p_Password.addEventListener("blur", (e) => {
            if (document.contains(document.getElementById("password-info"))) {
                document.getElementById("password-info").remove();
            }
        });
    }


    if (document.contains(p_Login)) {
        p_Login.addEventListener("input", (e) => {
            testLoginRegex(regex_2, p_Login);
        });
    }


    if (document.contains(pi_Password)) {
        pi_Password.addEventListener("input", (e) => {
            testLoginRegex(regex_3, pi_Password);
        });
    }


    function testRegex(regex, param) {
        if (!regex.test(param.value)) {
            param.classList.add("invalidData");
            document.getElementById("regBtn").disabled = true;

        }
        else {
            param.classList.remove("invalidData");
            document.getElementById("regBtn").disabled = false;
        }
    }

    function testLoginRegex(regex, param) {
        if (!regex.test(param.value)) {
            param.classList.add("invalidData");
            document.getElementById("loginBtn").disabled = true;

        }
        else {
            param.classList.remove("invalidData");
            document.getElementById("loginBtn").disabled = false;
        }
    }
});