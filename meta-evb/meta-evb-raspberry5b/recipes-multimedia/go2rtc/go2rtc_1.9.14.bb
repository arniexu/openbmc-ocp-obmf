SUMMARY = "go2rtc camera streaming gateway"
DESCRIPTION = "go2rtc bridges IP camera streams to RTSP, WebRTC, MP4, MJPEG, and ONVIF clients."
HOMEPAGE = "https://github.com/AlexxIT/go2rtc"
SECTION = "multimedia"

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://src/${GO_IMPORT}/LICENSE;md5=84f39ec0f1c900532a719dbb9ef7dfd3"

SRC_URI = "git://${GO_IMPORT}.git;protocol=https;nobranch=1;destsuffix=${GO_SRCURI_DESTSUFFIX} \
		   file://go2rtc.service \
		   file://go2rtc-discovery.service \
		   file://go2rtc-discovery.timer \
		   file://go2rtc.yaml \
		   file://go2rtc-discovery.conf \
		   file://go2rtc-discovery.py \
		   "

PV = "1.9.14"
SRCREV = "b5948cfb25404cc5cb37b166ecaa2dca20b11d4b"

require ${BPN}-licenses.inc
require ${BPN}-go-mods.inc

GO_IMPORT = "github.com/AlexxIT/go2rtc"
GO_INSTALL = "${GO_IMPORT}"
CGO_ENABLED = "0"
export GOPROXY = "https://proxy.golang.org,direct"
export HTTPS_PROXY = "http://child-prc.intel.com:913/"
export https_proxy = "${HTTPS_PROXY}"

inherit go-mod go2rtc-go-mod-update-modules systemd

GO_DYNLINK:aarch64 = ""
GO_LINKSHARED = ""
GO_RPATH = ""
GO_RPATH_LINK = ""

SYSTEMD_SERVICE:${PN} = "go2rtc.service go2rtc-discovery.service go2rtc-discovery.timer"

RDEPENDS:${PN} += " \
	python3-core \
	python3-json \
	python3-logging \
	python3-netclient \
"

CONFFILES:${PN} += " \
	${sysconfdir}/go2rtc/go2rtc.yaml \
	${sysconfdir}/default/go2rtc-discovery \
"

do_install() {
	install -d ${D}${bindir}
	install -d ${D}${libexecdir}/go2rtc
	install -d ${D}${sysconfdir}/go2rtc
	install -d ${D}${sysconfdir}/default
	install -d ${D}${systemd_system_unitdir}

	install -m 0755 ${B}/${GO_BUILD_BINDIR}/go2rtc ${D}${bindir}/go2rtc
	install -m 0644 ${UNPACKDIR}/go2rtc.yaml ${D}${sysconfdir}/go2rtc/go2rtc.yaml
	install -m 0644 ${UNPACKDIR}/go2rtc-discovery.conf ${D}${sysconfdir}/default/go2rtc-discovery
	install -m 0755 ${UNPACKDIR}/go2rtc-discovery.py ${D}${libexecdir}/go2rtc/go2rtc-discovery.py

	install -m 0644 ${UNPACKDIR}/go2rtc.service ${D}${systemd_system_unitdir}/go2rtc.service
	install -m 0644 ${UNPACKDIR}/go2rtc-discovery.service ${D}${systemd_system_unitdir}/go2rtc-discovery.service
	install -m 0644 ${UNPACKDIR}/go2rtc-discovery.timer ${D}${systemd_system_unitdir}/go2rtc-discovery.timer
}

FILES:${PN} += "${libexecdir}/go2rtc"

