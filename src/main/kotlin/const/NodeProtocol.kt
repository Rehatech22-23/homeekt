package const

enum class NodeProtocol(val id: Float) {
    NONE(0.0F),
    ZWAVE(1.0F),
    ZIGBEE(2.0F),
    ENOCEAN(3.0F),
    WMBUS(4F),
    HOMEMATIC(5F),
    KNXRF(6F),
    INOVA(7F),
    HTTPAVM(8F),
    HTTPNETATMO(9F),
    HTTPKOUBACHI(10F),
    HTTPNEST(11F),
    IOCUBE(12F),
    HTTPCCU2(13F),
    HTTPUPN_P(14F),
    HTTPNUKI(15F),
    HTTPSEMS(16F),
    SIGMA_ZWAVE(17F),
    LO_RA(18F),
    BI_SECUR(19F),
    HTTPWOLF(20F),
    EXTERNAL_HOMEE(21F),
    HTTPMYSTROM(24F),
    WMS(23F),
    ALL(100F)


}