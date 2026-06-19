const MAXIMALER_FOERDERANTEIL = 0.6;
const MAXIMALE_FOERDERSUMME = 30000;
const MINDESTFOERDERSUMME = 6000;

function beantragteFoerdersumme(gesamtausgaben, einnahmen) {
    if (gesamtausgaben < 0 || einnahmen < 0) {
        throw new Error("Ausgaben und Einnahmen dürfen nicht negativ sein.");
    }

    const finanzierungsbedarf = gesamtausgaben - einnahmen;
    if (finanzierungsbedarf <= 0) {
        return 0;
    }

    const foerdersumme = Math.min(
        gesamtausgaben * MAXIMALER_FOERDERANTEIL,
        MAXIMALE_FOERDERSUMME,
        finanzierungsbedarf
    );

    if (foerdersumme < MINDESTFOERDERSUMME) {
        return 0;
    }

    return foerdersumme;
}
module.exports = {
    beantragteFoerdersumme
};