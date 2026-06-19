export default {
    mutate: [
        "src/**/*.js"
    ],
    testRunner: "jest",
    reporters: ["html", "clear-text", "progress"],
    coverageAnalysis: "off"
};