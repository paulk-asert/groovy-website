import groovy.transform.ToString
import org.codehaus.groovy.control.CompilerConfiguration

@ToString(includeNames=true)
class SiteMap {
    final List<Section> documentationSections = []

    private SiteMap() {}

    public static SiteMap from(File source) {
        CompilerConfiguration config = new CompilerConfiguration()
        config.scriptBaseClass = 'groovy.util.DelegatingScript'
        GroovyShell shell = new GroovyShell(config)
        def script = shell.parse(source)

        def result = new SiteMap()
        script.setDelegate(result)
        script.run()

        result
    }

    private void documentation(Closure docSpec) {
        def clone = docSpec.rehydrate(this, this, this)
        clone()
    }

    private void section(String name, String icon, Closure sectionSpec) {
        Section section = new Section(name:name, icon:icon)
        def spec = sectionSpec.rehydrate(section,section,section)
        spec()
        documentationSections.add(section)
    }


}