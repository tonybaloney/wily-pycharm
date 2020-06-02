package wily.settings

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil

@State(name = "WilySettings", storages = [Storage("wily.xml")])
class WilySettings : PersistentStateComponent<WilySettings.State> {
    private val state: State = State()

    var maintainabilityIndexAlgorithm: MaintainabilityIndexAlgorithm
        get() = state.MAINTAINABILITY_INDEX_ALGORITHM
        set(value) {
            state.MAINTAINABILITY_INDEX_ALGORITHM = value
        }

    var weakWarningThreshold: Int
        get() = state.WEAK_WARNING_THRESHOLD
        set(value) {
            state.WEAK_WARNING_THRESHOLD = value
        }

    var warningThreshold: Int
        get() = state.WARNING_THRESHOLD
        set(value) {
            state.WARNING_THRESHOLD = value
        }

    var errorThreshold: Int
        get() = state.ERROR_THRESHOLD
        set(value) {
            state.ERROR_THRESHOLD = value
        }

    override fun getState(): State = state

    override fun loadState(state: State) {
        XmlSerializerUtil.copyBean(state, this.state)
    }

    @Suppress("PropertyName")
    class State {
        @JvmField
        var MAINTAINABILITY_INDEX_ALGORITHM: MaintainabilityIndexAlgorithm = MaintainabilityIndexAlgorithm.Classic
        var WEAK_WARNING_THRESHOLD: Int = 70
        var WARNING_THRESHOLD: Int = 30
        var ERROR_THRESHOLD: Int = 10
    }

    enum class MaintainabilityIndexAlgorithm {
        Classic,
        SEI,
        VS,
        Radon
    }

    companion object {
        @JvmStatic
        val instance: WilySettings
            get() = ServiceManager.getService(WilySettings::class.java)
    }
}