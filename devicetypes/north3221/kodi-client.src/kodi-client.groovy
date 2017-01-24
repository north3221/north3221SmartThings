import groovyjarjarantlr.collections.List

/**
 * Forked from https://github.com/Toliver182/SmartThings-Kodi who had
 * forked from a pelx version: https://github.com/iBeech/SmartThings/tree/master/PlexManager
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 *  I added some stuff like 'shutdown' so you can tell kodi to shutdown (the idea being it can turn off your TV)
 *  Also added better tracking of whats playing, I want to control the lights differently so I added some customer attributes
 */

//DEFAULTS
//Used for checking the kodi current playing metadata 'label' if word exists in teh label then 'movie category assigned
def getDefaultMovieLabels() {
    return "cinema, movie, film"
}
def getDefaultSportLabels() {
    def returnList = "sport"
    if (inputSportLabel){returnList = inputSportLabel}
    returnList.toLowerCase()
    return returnList
}
def getDefaultTVLabels() {
    def returnList = "bbc, itv, channel, sky"
    if (inputTVLabel){returnList = inputTVLabel}
    returnList.toLowerCase()
    return returnList
}
def getDefaultMinMovieRuntime() {
    if (inputMinMovieRuntime){return inputMinMovieRuntime}
    return 4200
}

metadata {
    definition (name: "Kodi-Client", namespace: "north3221", author: "north3221") {
        capability "Switch"
        capability "musicPlayer"
        capability "mediaController"

        command "scanNewClients"
        command "setPlaybackIcon", ["string"]
        command "setPlaybackTitle", ["string"]
        command "setVolumeLevel", ["number"]
        command "shutdown"
        command "describeAttributes"

        //custom attributes
        attribute "currentPlayingType", "string"
        attribute "currentPlayingCategory", "enum", ["Movie", "TV Show", "Sports", "None", "Unknown"]
        attribute "currentPlayingName", "string"
    }

    /*simulator {
        // TODO: define status and reply messages here
    }*/

    tiles(scale: 2) {
        def appListIcon = "http://forums.launchbox-app.com/uploads/monthly_2016_09/57d4171090e0e_Kodi2.thumb.png.fea39fca17f73c0c7bd0b81baed367aa.png"
        def mainIcon = "st.Electronics.electronics16"

        valueTile("appList", "device.status", width: 6, height: 2, canChangeIcon: false) {
            state "startup", label:'Startup', action:"music Player.play", icon:"${appListIcon}", backgroundColor:"#ddf4be"
            state "playing", label:'Playing', action:"music Player.pause", icon:"${appListIcon}", backgroundColor:"#79b821"
            state "stopped", label:'Stopped', action:"music Player.play", icon:"${appListIcon}", backgroundColor:"#ffffff"
            state "paused", label:'Paused', action:"music Player.play", icon:"${appListIcon}", backgroundColor:"#FFA500"
            state "shutdown", label:'Shutdown', action:"music Player.play", icon:"${appListIcon}", backgroundColor:"#ff0000"
        }

        standardTile("main", "device.status", width: 2, height: 2, canChangeIcon: true) {
            state "startup", label:'Startup', action:"music Player.play", icon:"${mainIcon}", backgroundColor:"#ddf4be"
            state "playing", label:'Playing', action:"music Player.pause", icon:"${mainIcon}", backgroundColor:"#79b821"
            state "stopped", label:'Stopped', action:"music Player.play", icon:"${mainIcon}", backgroundColor:"#ffffff"
            state "paused", label:'Paused', action:"music Player.play", icon:"${mainIcon}", backgroundColor:"#FFA500"
            state "shutdown", label:'Shutdown', action:"music Player.play", icon:"${mainIcon}", backgroundColor:"#ff0000"
        }

        standardTile("next", "device.status", width: 2, height: 2, decoration: "flat") {
            state "next", label:'', action:"music Player.nextTrack", icon:"st.sonos.next-btn", backgroundColor:"#ffffff"
        }

        standardTile("previous", "device.status", width: 2, height: 2, decoration: "flat") {
            state "previous", label:'', action:"music Player.previousTrack", icon:"st.sonos.previous-btn", backgroundColor:"#ffffff"
        }

        standardTile("scanNewClients", "device.status", width: 2, height: 1, decoration: "flat") {
            state "default", label:'', action:"scanNewClients", icon:"state.icon", backgroundColor:"#ffffff"
            state "grouped", label:'', action:"scanNewClients", icon:"state.icon", backgroundColor:"#ffffff"
        }

        standardTile("fillerTile", "device.status", width: 2, height: 2, decoration: "flat") {
            state "default", label:'', action:"", icon:"", backgroundColor:"#ffffff"
            state "grouped", label:'', action:"", icon:"", backgroundColor:"#ffffff"
        }

        standardTile("stop", "device.status", width: 2, height: 2, decoration: "flat") {
            state "default", label:'', action:"music Player.stop", icon:"st.sonos.stop-btn", backgroundColor:"#ffffff"
            state "grouped", label:'', action:"music Player.stop", icon:"st.sonos.stop-btn", backgroundColor:"#ffffff"
        }

        standardTile("shutdown", "device.status", width: 2, height: 2, decoration: "flat") {
            state "default", label:'shutdown', action:"shutdown", icon:"st.Electronics.electronics1", backgroundColor:"#ffffff"
            state "grouped", label:'shutdown', action:"shutdown", icon:"st.Electronics.electronics1", backgroundColor:"#ffffff"
        }

        valueTile("currentPlayingType", "device.currentPlayingType", inactiveLabel: true, height:1, width:3, decoration: "flat") {
            state "default", label:'${currentValue}', backgroundColor:"#ffffff"
        }
        valueTile("currentPlayingCategory", "device.currentPlayingCategory", inactiveLabel: true, height:1, width:3, decoration: "flat") {
            state "default", label:'${currentValue}', backgroundColor:"#ffffff"
        }
        valueTile("currentPlayingName", "device.currentPlayingName", inactiveLabel: true, height:2, width:6, decoration: "flat") {
            state "default", label:'${currentValue}', backgroundColor:"#ffffff"
        }

        controlTile("levelSliderControl", "device.level", "slider", height: 1, width: 6, inactiveLabel: false) {
            state "level", action:"setVolumeLevel", backgroundColor:"#ffffff"
        }

        main("appList")
        details(["currentPlayingType", "currentPlayingCategory", "currentPlayingName", "previous", "main", "next", "fillerTile", "stop", "shutdown", "levelSliderControl"])
    }

    preferences {
        input "inputMovieLabel", "text", required: true, title: "Movie labels: search kodi label for:", defaultValue: defaultMovieLabels, displayDuringSetup: false
        input "inputSportLabel", "text", required: true, title: "Sport labels: search kodi label for:", defaultValue: defaultSportLabels, displayDuringSetup: false
        input "inputTVLabel", "text", required: true, title: "TV labels: search kodi label for:", defaultValue: defaultTVLabels, displayDuringSetup: false
        input "inputMinMovieRuntime", "number", required: true, title: "Minimum Runtime to be classed as Move (seconds):", defaultValue: defaultMinMovieRuntime, displayDuringSetup: false
    }
}

// parse events into attributes
def parse(evt) {
    def msg = parseLanMessage(evt);

    if(msg.header){
        if(msg.header.contains("Unauthorized")){
            log.debug "Cannot authenticate: Please check kodi username and password"
            return
        }
    }


    if (!msg.body){
        return
    }


    if( msg.body == "{\"id\":1,\"jsonrpc\":\"2.0\",\"result\":\"OK\"}"){
        log.debug "recieved ok"
        return
    }

    if( msg.body == "{\"id\":1,\"jsonrpc\":\"2.0\",\"result\":{\"speed\":0}}"){
        log.debug "recieved ok"
        return
    }
    if( msg.body == "{\"id\":1,\"jsonrpc\":\"2.0\",\"result\":{\"speed\":1}}"){
        log.debug "recieved ok"
        return
    }
    if (msg.body == "{\"error\":{\"code\":-32100,\"message\":\"Failed to execute method.\"},\"id\":1,\"jsonrpc\":\"2.0\"}")
    {
        log.debug "error returned from kodi"
        return
    }

    if (msg.body.startsWith("{\"id\":\"VideoGetItem\"")) {
        parseNowPlaying(msg.body)
    }

}

def parseNowPlaying(msgBody){
    log.info "Parsing Now Playing"
    //Lists to check 'type' against to set category - I think this is the best way to validate type as this means kodi knows the type
    def tvShowType = ["episode"]
    def movieType = ["movie"]

    //start
    log.debug "Getting title, type and label"
    def slurper = new groovy.json.JsonSlurper().parseText(msgBody)
    def type = slurper.result.item.type
    log.info "Type = " + type
    def title = slurper.result.item.title

    //initialise category - Unknown so if not set stays as Unknown
    def category = "Unknown"
    def playingTitle = ""

    //If kodi doesnt know then let me try and work it out - else use what kodi says
    if (type == "unknown"){
        def label = slurper.result.item.label
        def runtime = slurper.result.item.runtime
        def plot = slurper.result.item.plot
        //Set movie label list
        //def movieLabel = defaultMovieLabels
        //Set sport label list
        def sportLabel = defaultSportLabels.split(',')
        //Set tv label list
        def tvShowLabel = defaultTVLabels.split(',')
        //Set min runtime to be a movie
        def minMovieRuntime = defaultMinMovieRuntime

        log.info "unknown type so checking label (" + label + ") contains Movie (" + movieLabels + ") or Sport (" + sportLabel + ") or TV Show (" + tvShowLabel + ")"
        //Check labels
        if (movieLabels.any {label.toLowerCase().contains(it)}) {
            category = "Movie"
        }else if(sportLabel.any {label.toLowerCase().contains(it)}) {
            category = "Sports"
        }else if(tvShowLabel.any {label.toLowerCase().contains(it)}) {
            category = "TV Show"
        }else if (runtime >= minMovieRuntime){
            category = "Movie"
        }else if (runtime > 0){
            category = "TV Show"
        }else if (plot.length() > 0){
            category = "Movie"
        }
        playingTitle = label
        log.info "Work out that category is (" + category + ")"
    } else if (movieType.any {type.toLowerCase().contains(it)}){
        category = "Movie"
        playingTitle = title
    } else if (tvShowType.any {type.toLowerCase().contains(it)}){
        def showTitle = slurper.result.item.showtitle
        category = "TV Show"
        playingTitle = showTitle + " : " + title
    }

    setPlaybackTitle(type, category, playingTitle)
    log.debug "Playing type is     :" + type
    log.debug "Playing category is :" + category
    log.debug "Playing title is    :" + playingTitle
}

def getMovieLabels() {
    def returnList = defaultMovieLabels
    if (inputMovieLabel) {
        log.info "Taking input = " + inputMovieLabel
        returnList = inputMovieLabel
    }
    returnList = returnList.toLowerCase().split(',').toList()
    log.info "Return List Finally = " + returnList
    return returnList
}

def play() {
    log.debug "Executing 'play'"

    sendEvent(name: "switch", value: device.deviceNetworkId + ".play");
    sendEvent(name: "switch", value: "on");
    sendEvent(name: "status", value: "playing");
}

def pause() {
    log.debug "Executing 'pause'"

    sendEvent(name: "switch", value: device.deviceNetworkId + ".pause");
    sendEvent(name: "switch", value: "off");
    sendEvent(name: "status", value: "paused");
}

def stop() {
    log.debug "Executing 'stop'"

    sendEvent(name: "switch", value: device.deviceNetworkId + ".stop");
    sendEvent(name: "switch", value: "off");
    sendEvent(name: "status", value: "stopped");
    //setPlaybackTitle("Stopped");
}

def shutdown() {
    log.debug "Executing 'stop'"

    sendEvent(name: "switch", value: device.deviceNetworkId + ".shutdown");
    sendEvent(name: "switch", value: "off");
    sendEvent(name: "status", value: "shutdown");
    //setPlaybackTitle("Shutdown");
}

def previousTrack() {
    log.debug "Executing 'previous': "

    //setPlaybackTitle("Skipping previous");
    sendCommand("previous");
}

def nextTrack() {
    log.debug "Executing 'next'"

    //setPlaybackTitle("Skipping next");
    sendCommand("next");
}

def scanNewClients() {
    log.debug "Executing 'scanNewClients'"
    sendCommand("scanNewClients");
}

def setVolumeLevel(level) {
    log.debug "Executing 'setVolumeLevel(" + level + ")'"
    sendEvent(name: "level", value: level);
    sendCommand("setVolume." + level);
}

def sendCommand(command) {

    def lastState = device.currentState('switch').getValue();
    sendEvent(name: "switch", value: device.deviceNetworkId + "." + command);
    sendEvent(name: "switch", value: lastState);
}

def setPlaybackState(state) {

    log.debug "Setting playback state to: " + state
    switch(state) {
        case "stopped":
            sendEvent(name: "switch", value: "off");
            sendEvent(name: "status", value: "stopped");
            setPlaybackTitle("","","")
            break;

        case "playing":
            sendEvent(name: "switch", value: "on");
            sendEvent(name: "status", value: "playing");
            break;

        case "paused":
            sendEvent(name: "switch", value: "off");
            sendEvent(name: "status", value: "paused");
            break;

        case "shutdown":
            sendEvent(name: "switch", value: "off");
            sendEvent(name: "status", value: "shutdown");
            setPlaybackTitle("","", "")
            break;

        case "startup":
            sendEvent(name: "switch", value: "off");
            sendEvent(name: "status", value: "startup");
            setPlaybackTitle("","", "")
            break;
    }
}

def setPlaybackTitle(type, category, name) {

    if(type == ""){
        type = 'None'
    }
    if(category == ""){
        category = 'None'
    }
    if(name == ""){
        name = 'Nothing Playing'
    }

    log.debug "Setting title to :" + name
    sendEvent(name: "currentPlayingType", value: type)
    sendEvent(name: "currentPlayingCategory", value: category)
    sendEvent(name: "currentPlayingName", value: name)
}

def setPlaybackIcon(iconUrl) {
    log.debug "Executing 'setPlaybackIcon'"

    state.icon = iconUrl;

    //sendEvent(name: "scanNewClients", icon: iconUrl)
    //sendEvent(name: "scanNewClients", icon: iconUrl)

    log.debug "Icon set to " + state.icon
}

//define attributes for CoRE
def describeAttributes(payload) {
    payload.attributes = [
            [ name: "currentPlayingType", type: "string"],
            [ name: "currentPlayingCategory", type: "enum", options: ["Movie", "TV Show", "Sports", "None", "Unknown"]],
            [ name: "currentPlayingName", type: "string"]
    ]
    return null
}
