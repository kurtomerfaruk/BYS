/*
 * Stok karti barkod okutma yardimcisi.
 * html5-qrcode (Apache-2.0) kutuphanesini kullanir.
 * Klavye emulasyonlu barkod okuyucu zaten Enter ile calisir; bu dosya
 * telefon/tablet kamerasindan okutma akisini yonetir.
 */
var stBarkodScanner = (function () {

    var scanner = null;
    var inputId = null;
    var buttonId = null;

    function input() {
        return inputId ? document.getElementById(inputId) : null;
    }

    function addButton() {
        return buttonId ? document.getElementById(buttonId) : null;
    }

    function stop() {
        if (!scanner) {
            return Promise.resolve();
        }
        var current = scanner;
        scanner = null;
        try {
            return current.stop().then(function () {
                current.clear();
            }).catch(function () {
                // kamera zaten kapali olabilir
            });
        } catch (e) {
            return Promise.resolve();
        }
    }

    function start(containerId) {
        if (typeof Html5Qrcode === "undefined") {
            window.alert("Barkod okuma kutuphanesi yuklenemedi.");
            return;
        }
        if (!scanner) {
            scanner = new Html5Qrcode(containerId, {verbose: false});
        }
        Html5Qrcode.getCameras().then(function (cameras) {
            if (!cameras || cameras.length === 0) {
                window.alert("Kamera bulunamadi.");
                return;
            }
            scanner.start(
                {facingMode: "environment"},
                {fps: 10, qrbox: {width: 250, height: 160}},
                onScan,
                function () {
                }
            ).catch(function (err) {
                window.alert("Kamera baslatilamadi: " + err);
            });
        }).catch(function () {
            window.alert("Kamera erisimi alinamadi. HTTPS veya localhost uzerinden acin.");
        });
    }

    function onScan(decodedText) {
        var field = input();
        if (field) {
            field.value = decodedText;
        }
        stop().then(function () {
            if (window.PF && PF("StBarkodScannerDialog")) {
                PF("StBarkodScannerDialog").hide();
            }
            var button = addButton();
            if (button) {
                button.click();
            }
        });
    }

    function ac(scannerInputId) {
        inputId = scannerInputId;
        buttonId = scannerInputId ? scannerInputId.replace(/(yeniBarkod|barkodOku)$/, "btnBarkodEkle") : null;
        if (!inputId) {
            window.alert("Barkod alani bulunamadi.");
            return;
        }
        if (window.PF && PF("StBarkodScannerDialog")) {
            PF("StBarkodScannerDialog").show();
        }
        // Dialog DOM'a eklendikten sonra kamerayi baslat
        window.setTimeout(function () {
            start("stBarkodScannerContainer");
        }, 250);
    }

    function kapat() {
        stop().then(function () {
            if (window.PF && PF("StBarkodScannerDialog")) {
                PF("StBarkodScannerDialog").hide();
            }
        });
    }

    return {
        ac: ac,
        kapat: kapat
    };
})();
