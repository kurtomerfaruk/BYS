/*
 * Sayım girişleri için tarayıcı tarafı yedek.
 * Sunucuya yazım anında yapılır; bu dosya ek güvenlik/amacıyla
 * son girişleri localStorage'da tutar ve kullanıcı onayıyla geri yükler.
 * Fonksiyon adları İngilizcedir.
 */
var stCountBackup = (function () {

    var PREFIX = "bys_sayim_";

    function storageKey(sayimId) {
        return PREFIX + sayimId;
    }

    function save(sayimId, code, quantity) {
        if (!sayimId || !code) {
            return;
        }
        var cleanCode = String(code).replace(/[|;]/g, "_");
        var entry = cleanCode + "|" + quantity;
        var key = storageKey(sayimId);
        var current = window.localStorage.getItem(key) || "";
        var entries = current ? current.split(";") : [];
        var updated = [];
        for (var i = 0; i < entries.length; i++) {
            var parts = entries[i].split("|");
            if (parts[0] !== cleanCode && entries[i]) {
                updated.push(entries[i]);
            }
        }
        updated.push(entry);
        if (updated.length > 500) {
            updated = updated.slice(updated.length - 500);
        }
        window.localStorage.setItem(key, updated.join(";"));
    }

    function read(sayimId) {
        if (!sayimId) {
            return "";
        }
        return window.localStorage.getItem(storageKey(sayimId)) || "";
    }

    function hasData(sayimId) {
        return read(sayimId).length > 0;
    }

    function clear(sayimId) {
        if (!sayimId) {
            return;
        }
        window.localStorage.removeItem(storageKey(sayimId));
    }

    function apply(sayimId, remoteCommandName) {
        var data = read(sayimId);
        if (!data || !window.PF || !PF(remoteCommandName)) {
            return false;
        }
        PF(remoteCommandName)([{name: "backupData", value: data}]);
        return true;
    }

    return {
        save: save,
        read: read,
        hasData: hasData,
        clear: clear,
        apply: apply
    };
})();