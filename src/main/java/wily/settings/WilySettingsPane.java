package wily.settings;

import javax.swing.*;

public class WilySettingsPane {

    private JPanel settingsPanel;
    private JComboBox miAlgorithmCombo;
    private JSlider slider1;
    private JSlider slider2;
    private JSlider slider3;


    public JPanel getPanel() {
        return settingsPanel;
    }

    public void storeSettings(WilySettings settings) {
        settings.setWeakWarningThreshold(slider1.getValue());
        settings.setWarningThreshold(slider2.getValue());
        settings.setErrorThreshold(slider3.getValue());
        settings.setMaintainabilityIndexAlgorithm(getMaintainabilityIndexAlgorithm());
    }

    private WilySettings.MaintainabilityIndexAlgorithm getMaintainabilityIndexAlgorithm(){
        switch (miAlgorithmCombo.getSelectedIndex()){
            case 0: return WilySettings.MaintainabilityIndexAlgorithm.Classic;
            case 1: return WilySettings.MaintainabilityIndexAlgorithm.SEI;
            case 2: return WilySettings.MaintainabilityIndexAlgorithm.VS;
            case 3: return WilySettings.MaintainabilityIndexAlgorithm.Radon;
        }
        return WilySettings.MaintainabilityIndexAlgorithm.Classic;
    }

    private void setMaintainabilityIndexAlgorithm(WilySettings.MaintainabilityIndexAlgorithm algorithm){
        switch (algorithm){
            case Classic: miAlgorithmCombo.setSelectedIndex(0);
            case SEI: miAlgorithmCombo.setSelectedIndex(1);
            case VS: miAlgorithmCombo.setSelectedIndex(2);
            case Radon: miAlgorithmCombo.setSelectedIndex(3);
        }
    }

    public boolean isModified(WilySettings settings) {
        return !settings.getMaintainabilityIndexAlgorithm().equals(getMaintainabilityIndexAlgorithm()) ||
                settings.getWeakWarningThreshold() != slider1.getValue() ||
                settings.getWarningThreshold() != slider2.getValue() ||
                settings.getErrorThreshold() != slider3.getValue();
    }

    public void setData(WilySettings settings) {
        setMaintainabilityIndexAlgorithm(settings.getMaintainabilityIndexAlgorithm());
        slider1.setValue(settings.getWeakWarningThreshold());
        slider2.setValue(settings.getWarningThreshold());
        slider3.setValue(settings.getErrorThreshold());
    }
}
