package wily

import com.intellij.lang.annotation.HighlightSeverity
import wily.settings.WilySettings
import kotlin.math.ln
import kotlin.math.log2
import kotlin.math.sqrt

fun MI (V: Double, G: Int, LOC: Int): Double {
    val C: Double = .0
    return when (WilySettings.instance.maintainabilityIndexAlgorithm) {
        WilySettings.MaintainabilityIndexAlgorithm.Classic -> classic(V, G, LOC)
        WilySettings.MaintainabilityIndexAlgorithm.SEI -> sei(V, G, LOC, C)
        WilySettings.MaintainabilityIndexAlgorithm.VS -> vs(V, G, LOC)
        else -> radon(V, G, LOC, C)
    }
}

private fun classic(V: Double, G: Int, LOC: Int): Double {
    return 171 - 5.2 * ln(V) - 0.23 * (G) - 16.2 * ln(LOC.toDouble())
}

private fun sei(V: Double, G: Int, LOC: Int, C: Double = .0): Double {
    return 171 - 5.2 * log2(V) - 0.23 * (G) - 16.2 * log2(LOC.toDouble()) + 50 * kotlin.math.sin(sqrt(2.4 * C))
}

private fun vs(V: Double, G: Int, LOC: Int): Double {
    return (171 - 5.2 * ln(V) - 0.23 * (G) - 16.2 * ln(LOC.toDouble()))/171
}

private fun radon(V: Double, G: Int, LOC: Int, C: Double = .0): Double {
    return (171 - 5.2 * log2(V) - 0.23 * (G) - 16.2 * log2(LOC.toDouble()) + 50 * kotlin.math.sin(sqrt(2.4 * C)))/171
}

fun asHighlightingSeverity(mi: Int): HighlightSeverity {
    return when {
        mi > WilySettings.instance.weakWarningThreshold -> {
            HighlightSeverity.INFORMATION
        }
        mi > WilySettings.instance.warningThreshold -> {
            HighlightSeverity.WEAK_WARNING
        }
        mi > WilySettings.instance.errorThreshold -> {
            HighlightSeverity.WARNING
        }
        else -> {
            HighlightSeverity.ERROR
        }
    }
}