/*
 * Genel amaçlı, sunucuya gitmeyen tablo satır filtresi.
 * Fonksiyon adı İngilizcedir.
 */
function filterTableRows(input, tableClientId) {
    var query = ((input && input.value) || "").toLowerCase();
    var selector = "#" + tableClientId.replace(/:/g, "\\:") + " tbody tr";
    jQuery(selector).each(function () {
        var text = jQuery(this).text().toLowerCase();
        jQuery(this).toggle(query === "" || text.indexOf(query) >= 0);
    });
}