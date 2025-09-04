function addLayer(layer) {
    mapVar.addLayer(layer);
}

function removeLayer(layer) {
    mapVar.removeLayer(layer);
}

function refreshHeatMap(dataCount,dataJson){
    var newData = { max: dataCount, data:dataJson};
    removeLayer(heatmapLayer);
    setTimeout(function (){
        addLayer(heatmapLayer);
        heatmapLayer.setData(newData);
    },500);
}





