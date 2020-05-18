var myMap;

// Дождёмся загрузки API и готовности DOM.
ymaps.ready(init);

function init() {
    // Создание экземпляра карты и его привязка к контейнеру с
    // заданным id ("map").
    myMap = new ymaps.Map('map', {
        // При инициализации карты обязательно нужно указать
        // её центр и коэффициент масштабирования.
        center: [55.76, 37.64], // Москва
        zoom: 10,
        controls: ['geolocationControl']

    }, {
        searchControlProvider: 'yandex#search'
    });

    let response = fetch("http://localhost:8080/rest/cars", {
        method: 'GET',
        mode: 'cors',
        credentials: 'include'
    }).then(response => response.json())
        // .then(json => addToObjectManager(json));
        .then(json => remakeMap(json));


}


function remakeMap(json) {
    console.log(json);
    // myMap.geoObjects.var
    myGeoObjects = new ymaps.GeoObjectCollection(null, {
        preset: "islands#redCircleIcon",
    });

    json.forEach(item =>
        myGeoObjects.add(new ymaps.Placemark([item['lng'], item['ltd']], {
            balloonContentHeader: "<a href='/cars/" + item['id'] + "'>" + "<span>" + item['model'] + "</span></a>",
            balloonContentBody: item['cost'],
            balloonContentFooter: "<a href='/profile/" + item['owner']['id'] + "'>" + item['owner']['email'] + "</a>"
        })));
    myMap.geoObjects.add(myGeoObjects);
}

function addToObjectManager(json) {
    var objectManager = new ymaps.ObjectManager({
        // Включаем кластеризацию.
        clusterize: true,
    });
    var myGeoObjects = new ymaps.GeoObjectCollection(null, {
        preset: "islands#redCircleIcon",
    });
    objectManager.objects.options.set('preset', 'islands#grayIcon');
    json.forEach(item =>
        myGeoObjects.add(new ymaps.Placemark([item['lng'], item['ltd']])));
    objectManager.add(myGeoObjects);
    myMap.geoObjects.add(objectManager);
}

function sleep(ms) {
    ms += new Date().getTime();
    while (new Date() < ms) {
    }
}