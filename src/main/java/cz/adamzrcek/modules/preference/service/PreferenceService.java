package cz.adamzrcek.modules.preference.service;

import cz.adamzrcek.modules.preference.dtos.PreferenceDto;
import cz.adamzrcek.modules.preference.dtos.PreferenceNewRequest;

import java.util.List;

public interface PreferenceService {
    PreferenceDto getPreference(Long id);
    PreferenceDto createPreference(PreferenceNewRequest request);
    List<PreferenceDto> getAllPreferencesForSignedUser();
    List<PreferenceDto> getAllPreferencesForSignedUserByCategory(Long categoryId);
    List<PreferenceDto> getPartnerPreferences();
    List<PreferenceDto> getPartnerPreferencesByCategory(Long categoryId);
    void deletePreference(Long id);
    PreferenceDto updatePreference(Long id, PreferenceNewRequest request);

}
