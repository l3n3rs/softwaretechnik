const { beantragteFoerdersumme } = require("./src/foerdersumme.js");

describe("beantragteFoerdersumme", () => {
    test("maximale Fördersumme beträgt 30.000 Euro", () => {
        expect(beantragteFoerdersumme(100000, 0)).toBe(30000);
    });

    test("maximal 60 Prozent der Gesamtausgaben werden gefördert", () => {
        expect(beantragteFoerdersumme(20000, 0)).toBe(12000);
    });

    test("Einnahmen reduzieren den Finanzierungsbedarf", () => {
        expect(beantragteFoerdersumme(20000, 10000)).toBe(10000);
    });

    test("keine Förderung, wenn Finanzierungsbedarf 0 ist", () => {
        expect(beantragteFoerdersumme(10000, 10000)).toBe(0);
    });

    test("keine Förderung, wenn Einnahmen höher als Gesamtausgaben sind", () => {
        expect(beantragteFoerdersumme(10000, 12000)).toBe(0);
    });

    test("keine Förderung, wenn mögliche Förderung unter 6.000 Euro liegt", () => {
        expect(beantragteFoerdersumme(9000, 0)).toBe(0);
    });

    test("Förderung möglich, wenn Mindestfördersumme genau 6.000 Euro beträgt", () => {
        expect(beantragteFoerdersumme(10000, 0)).toBe(6000);
    });

    test("Gesamtausgaben dürfen nicht negativ sein", () => {
        expect(() => beantragteFoerdersumme(-1000, 0)).toThrow(
            "Ausgaben und Einnahmen dürfen nicht negativ sein."
        );
    });

    test("Einnahmen dürfen nicht negativ sein", () => {
        expect(() => beantragteFoerdersumme(10000, -1000)).toThrow(
            "Ausgaben und Einnahmen dürfen nicht negativ sein."
        );
    });
    /* Auskommentiert für Test mit Stryker
        describe("aktuelle Schwächen bei null-Werten", () => {
    
            test("Gesamtausgaben null wird aktuell nicht als Fehler behandelt", () => {
                expect(() => beantragteFoerdersumme(null, 0)).not.toThrow();
            });
    
            test("Einnahmen dürfen nicht null sein", () => {
                expect(() => beantragteFoerdersumme(10000, null)).toThrow();
            });
        });
    */

});


