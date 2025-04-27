package cz.adamzrcek.service;

import cz.adamzrcek.dtos.preference.PreferenceDto;
import cz.adamzrcek.dtos.preference.PreferenceNewRequest;

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
