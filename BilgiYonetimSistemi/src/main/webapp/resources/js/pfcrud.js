function handleSubmit(xhr, status, args, dialog) {
    var dialogId = dialog.id.replaceAll(":", "\\\:");
    if (args.validationFailed) {
        $("#" + dialogId).effect("shake", {times: 3}, 100);
    } else {
        dialog.hide();
    }
}

function fixPFMDialogs() {
    jQuery("body > div[data-role='page']").each(function (i) {
        var pageId = jQuery(this).attr("id");
        jQuery("body > div[id*='" + pageId + "'][class*='ui-popup']").appendTo("#" + pageId);
    });
}

function dialogAc(dialogName) {
    var open = PF('exceptionDialog').isVisible();
    if (open) {
        PF(dialogName).hide();
    } else {
        PF(dialogName).show();
    }
}

function isNumber(evt) {
    evt = (evt) ? evt : window.event;
    var charCode = (evt.which) ? evt.which : evt.keyCode;
    if (charCode > 31 && (charCode < 48 || charCode > 57)) {
        return false;
    }
    return true;
}

function adjustMenuWidth(dialogWidget,componentClass,width = 30) {
    var dialog = PF(dialogWidget);
    if (dialog) {
        var dialogWidth = dialog.jq.innerWidth();
        $("."+componentClass).width(dialogWidth-width);
    }
}


PrimeFaces.widget.DataTable.prototype.adjustScrollHeight = function () {
    var relativeHeight = this.jq.parent().innerHeight() * (parseInt(this.cfg.scrollHeight) / 100),
        headerChilden = this.jq.children('.ui-datatable-header'),
        footerChilden = this.jq.children('.ui-datatable-footer'),
        tableHeaderHeight = (headerChilden.length > 0) ? headerChilden.outerHeight(true) : 0,
        tableFooterHeight = (footerChilden.length > 0) ? footerChilden.outerHeight(true) : 0,
        scrollersHeight = (this.scrollHeader.outerHeight(true) + this.scrollFooter.outerHeight(true)),
        paginatorsHeight = this.paginator ? this.paginator.getContainerHeight(true) : 0,
        height = (relativeHeight - (scrollersHeight + paginatorsHeight + tableHeaderHeight + tableFooterHeight));

    if (this.cfg.virtualScroll) {
        this.scrollBody.css('max-height', height);
    } else {
        this.scrollBody.height(height);
    }
};

PrimeFaces.validator['TcKimlikNo'] = {
    throwError: function (detail) {
        throw {
            summary: 'Doğrulama Hatası',
            detail: detail
        }
    },
    isValid: function (value) {
        if (value.match("^\\d{11}$") == null) {
            return false;
        }
        digits = new Array(11);
        for (i = 0; i < digits.length; ++i) {
            digits[i] = value.charCodeAt(i) - 48;
            if (digits[i] < 0 || digits[i] > 9) {
                return false;
            }
        }
        x = digits[0];
        y = digits[1];
        for (i = 1; i < 5; i++) {
            x += Number(digits[2 * i]);
        }
        for (i = 2; i <= 4; i++) {
            y += Number(digits[2 * i - 1]);
        }
        c1 = 7 * x - y;
        if (c1 % 10 != digits[9]) {
            return false;
        }
        c2 = 0;
        for (i = 0; i < 10; ++i) {
            c2 += digits[i];
        }
        if (c2 % 10 != digits[10]) {
            return false;
        }
        return true;
    },
    validate: function (element, value) {
        if (value == undefined || value.length != 11) {
            this.throwError("Girmiş olduğunuz TC Kimlik No hatalı");
        }
        if (!this.isValid(value)) {
            messageId = element.data('message');
            message = PrimeFaces.util.ValidationContext.getMessage(messageId).detail;
            this.throwError(message);
        }
    }
};

if (!PrimeFaces.validator['StrongPassword']) {
    PrimeFaces.validator['StrongPassword'] = {

        pattern: {
            upper: /[A-Z]/,
            lower: /[a-z]/,
            number: /[0-9]/,
            special: /[^a-zA-Z0-9]/
        },

        validate: function(element, value) {
            var $element = $(element);
            var cfg = $element.data('p-strongpassword');
            console.log(cfg);

            var minLength = cfg && cfg.minLength ? cfg.minLength : 8;
            var requireUpper = !(cfg && cfg.requireUpper === false);
            var requireLower = !(cfg && cfg.requireLower === false);
            var requireNumber = !(cfg && cfg.requireNumber === false);
            var requireSpecial = !(cfg && cfg.requireSpecial === false);

            if (!value) {
                throw {
                    summary: 'Validation Error',
                    detail: 'Password cannot be empty'
                };
            }

            if (value.length < minLength) {
                throw {
                    summary: 'Validation Error',
                    detail: 'Password must be at least ' + minLength + ' characters long1'
                };
            }

            if (requireUpper && !this.pattern.upper.test(value)) {
                throw {
                    summary: 'Validation Error',
                    detail: 'Password must contain at least one uppercase letter'
                };
            }

            if (requireLower && !this.pattern.lower.test(value)) {
                throw {
                    summary: 'Validation Error',
                    detail: 'Password must contain at least one lowercase letter'
                };
            }

            if (requireNumber && !this.pattern.number.test(value)) {
                throw {
                    summary: 'Validation Error',
                    detail: 'Password must contain at least one number'
                };
            }

            if (requireSpecial && !this.pattern.special.test(value)) {
                throw {
                    summary: 'Validation Error',
                    detail: 'Password must contain at least one special character'
                };
            }
        }
    };
}

PrimeFaces.validator['custom.emailValidator'] = {
    pattern: /\S+@\S+/,
    validate: function (element, value) {
        //use element.data() to access validation metadata, in this case there is none.
        if (!this.pattern.test(value)) {
            throw {
                summary: 'Validation Error',
                detail: value + ' is not a valid email.'
            }
        }
    }
};

/**
 * Bean validator
 */
PrimeFaces.validator['Email'] = {
    pattern: /\S+@\S+/,
    MESSAGE_ID: 'org.primefaces.examples.validate.email.message',
    validate: function (element, value) {
        var vc = PrimeFaces.util.ValidationContext;

        if (!this.pattern.test(value)) {
            var msgStr = element.data('p-email-msg'),
                msg = msgStr ? {summary: msgStr, detail: msgStr} : vc.getMessage(this.MESSAGE_ID);

            throw msg;
        }
    }
};

function counter() {
    var counter = document.querySelectorAll(".counter-value");
    var speed = 250; // The lower the slower
    counter &&
    counter.forEach(function (counter_value) {
        function updateCount() {
            var target = +counter_value.getAttribute("data-target");
            var count = +counter_value.innerText;
            var inc = target / speed;
            if (inc < 1) {
                inc = 1;
            }
            // Check if target is reached
            if (count < target) {
                // Add inc to count and output in counter_value
                counter_value.innerText = (count + inc).toFixed(0);
                // Call function every ms
                setTimeout(updateCount, 1);
            } else {
                counter_value.innerText = numberWithCommas(target);
            }
            numberWithCommas(counter_value.innerText);
        }

        updateCount();
    });

    function numberWithCommas(x) {
        return x.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    }
}

function start() {
    PF('Loading').show();
}

function stop() {
    PF('Loading').hide();
}

//
// function toggleSidebar() {
//     const sidebar = PF('menuSidebar');
//     const button = $(".left-fixed-button")
//
//     if (sidebar.isVisible()) {
//         sidebar.hide(); // Sidebar kapatılıyor
//         resetButtonPosition(); // Buton pozisyonu resetleniyor
//     } else {
//         sidebar.show(); // Sidebar açılıyor
//         //button.style.right = '320px'; // Sidebar genişliği + boşluk kadar sola kaydır
//         button.css("right","300px")
//     }
// }
//
// function resetButtonPosition() {
//     const button = $(".left-fixed-button");
//     button.css("right","00px")
// }

let createpassword = (type, ele) => {
    document.getElementById(type).type = document.getElementById(type).type == "password" ? "text" : "password"
    let icon = ele.childNodes[1].classList
    let stringIcon = icon.toString()
    if (stringIcon.includes("ri-eye-line")) {
        ele.childNodes[1].classList.remove("ri-eye-line")
        ele.childNodes[1].classList.add("ri-eye-off-line")
    }
    else {
        ele.childNodes[1].classList.add("ri-eye-line")
        ele.childNodes[1].classList.remove("ri-eye-off-line")
    }
}

function fitTabWidth(div){
    var width = $("#"+div).width();
    $(".tab-pane").width(width);
}

PrimeFacesExt.locales.TimeAgo['tr'] = {
    prefixAgo: null,
    prefixFromNow: "over",
    suffixAgo: 'önce',
    suffixFromNow: null,
    seconds: 'birkaç saniye',
    minute: '1 dakika',
    minutes: '%d dakika',
    hour: '1 saat',
    hours: '%d saat',
    day: '1 gün',
    days: '%d gün',
    month: '1 ay',
    months: '%d ay',
    year: '1 yıl',
    years: '%d yıl',
    wordSeparator: " ",
    numbers: []
}
