package wily.settings

import com.intellij.openapi.options.Configurable
import com.intellij.openapi.options.ConfigurationException
import com.intellij.openapi.options.SearchableConfigurable
import org.jetbrains.annotations.Nls
import javax.swing.*

class WilySettingsConfigurable : SearchableConfigurable, Configurable.NoScroll {
    private var settingsPanel: WilySettingsPane =
        WilySettingsPane()

    @Nls
    override fun getDisplayName(): String? {
        return "Wily"
    }

    override fun getHelpTopic(): String? {
        return null
    }

    override fun createComponent(): JComponent? {
        settingsPanel.setData(WilySettings.instance)
        return settingsPanel.getPanel()
    }

    override fun isModified(): Boolean {
        return settingsPanel.isModified(WilySettings.instance)
    }

    @Throws(ConfigurationException::class)
    override fun apply() {
        settingsPanel.storeSettings(WilySettings.instance)
    }

    override fun reset() {
        // TODO
    }

    override fun disposeUIResources() {
    }

    override fun getId(): String {
        return "wily.wily-pycharm.settings.WilySettingsConfigurable"
    }
}