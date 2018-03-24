package com.github.frkator.visualdebugger.jdi.wrapper.processor.impl.events.specification;

public class SettingsSpecification {

    //za strateđi
    //whitelist/blacklist - za ovo izgleda treba poznavat source
    //bkpt bez zadane varijable snima sve ili ne?
    //proslijediti originalni eventž?
    //dfd timestamp?

    //todo bacit excep is listenera

    //napisat blacklist i whitelist testove ' dokazat da blacklist i whitelist se ne može izvest napravit specification za classprepreq odnosno moye whitelist i snimaj sve
    //dovrsit classunload
    //napisat util metodu za init testova
    // dodat resetireanje debug agenta da se ne mora vm reinitat svaki put između test metode
    // ispitat whitelist i blacklist do kraja
    // final na sve evt i evtreq procove
    // rinejmat sve evtprocove u jdievtprocove
    // dodat noconf implementaciju jdieventconf za vmstartstop proceove
    //u jdievtreq stavljat konfove umjesto niza parametara čitkije je
}
