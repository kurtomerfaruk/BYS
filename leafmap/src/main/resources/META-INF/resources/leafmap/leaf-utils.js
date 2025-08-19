function getColor(d, values) {
    return d > values[values.length - 1] ? '#800026' :
        d > values[values.length - 2] ? '#BD0026' :
            d > values[values.length - 3] ? '#E31A1C' :
                d > values[values.length - 4] ? '#FC4E2A' :
                    d > values[values.length - 5] ? '#FD8D3C' :
                        d > values[values.length - 6] ? '#FEB24C' :
                            d > values[values.length - 7] ? '#FED976' : '#FFEDA0';
}

function style(feature) {
    return {
        weight: 2,
        opacity: 1,
        color: 'white',
        dashArray: '3',
        fillOpacity: 0.7,
        fillColor: getColor(feature.properties.density)
    };
}
//
// function highlightFeature(e) {
//     const layer = e.target;
//
//     layer.setStyle({
//         weight: 5,
//         color: '#666',
//         dashArray: '',
//         fillOpacity: 0.7
//     });
//
//     layer.bringToFront();
//
//     info.update(layer.feature.properties);
// }
