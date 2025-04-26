package cz.adamzrcek.service;

import cz.adamzrcek.dtos.preference.PreferenceDto;
import cz.adamzrcek.dtos.preference.PreferenceNewRequest;
import cz.adamzrcek.entity.Preference;
import cz.adamzrcek.entity.User;
import cz.adamzrcek.exception.NotAllowedException;
import cz.adamzrcek.exception.PreferenceNotFoundException;
import cz.adamzrcek.exception.ResourceNotFoundException;
import cz.adamzrcek.repository.PreferenceCategoryRepository;
import cz.adamzrcek.repository.PreferenceRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class PreferenceService {
    private final PreferenceRepository preferenceRepository;
    private final UserService userService;
    private final ConnectionService connectionService;
    private final PreferenceCategoryRepository preferenceCategoryRepository;

    public PreferenceDto getPreference(Long id){
        Preference preference = getPreferenceById(id);
        validateUserAccess(preference, "access");
        return toDto(preference);
    }

    public PreferenceDto createPreference(PreferenceNewRequest request){
        User currentUser = getCurrentUser();

        Preference preference = Preference.builder()
                .category(
                        preferenceCategoryRepository.findById(request.getCategoryId())
                                .orElseThrow(() -> new ResourceNotFoundException("Preference category with id " + request.getCategoryId() + " not found")))
                .value(request.getValue())
                .user(currentUser).build();

        log.debug("User {} created new preference with category {}", currentUser.getId(), preference.getCategory());

        return toDto(preferenceRepository.save(preference));
    }

    public List<PreferenceDto> getAllPreferencesForSignedUser(){
        return getAllPreferences(getCurrentUser());
    }

    public List<PreferenceDto> getAllPreferencesForSignedUserByCategory(Long categoryId){
        return getAllPreferencesByCategory(categoryId, getCurrentUser());
    }

    public List<PreferenceDto> getPartnerPreferences(){
        User partner = getConnectedPartner();
        return getAllPreferences(partner);
    }

    public List<PreferenceDto> getPartnerPreferencesByCategory(Long categoryId){
        User partner = getConnectedPartner();
        return getAllPreferencesByCategory(categoryId, partner);
    }

    public void deletePreference(Long id){
        Preference preference = getPreferenceById(id);
        validateUserAccess(preference, "delete");

        log.debug("User {} deleted preference {}", getCurrentUser().getId(), preference.getCategory());

        preferenceRepository.delete(preference);
    }

    public PreferenceDto updatePreference(Long id, PreferenceNewRequest request){
        Preference preference = getPreferenceById(id);
        validateUserAccess(preference, "update");

        preference.setCategory(
                preferenceCategoryRepository.findById(request.getCategoryId())
                        .orElseThrow(() -> new ResourceNotFoundException("Preference category with id " + request.getCategoryId() + " not found")));
        preference.setValue(request.getValue());

        log.debug("User {} updated preference {}", getCurrentUser().getId(), preference.getCategory());

        return toDto(preferenceRepository.save(preference));
    }

    private User getCurrentUser() {
        return userService.getCurrentUser();
    }

    private Preference getPreferenceById(Long id) {
        return preferenceRepository.findById(id)
                .orElseThrow(() -> new PreferenceNotFoundException("Preference with id " + id + " not found"));
    }

    private void validateUserAccess(Preference preference, String action) {
        if (!preference.getUser().equals(getCurrentUser())) {
            throw new NotAllowedException("User is not allowed to " + action + " this preference");
        }
    }

    private User getConnectedPartner() {
        var connection = connectionService.getCurrentConnection();
        if (connection == null) {
            throw new NotAllowedException("User is not connected to anyone");
        }
        return userService.getUserById(connection.partner().id());
    }

    private List<PreferenceDto> getAllPreferencesByCategory(Long category, User user){
        return preferenceRepository.findAllByUserAndCategory_Id(user, category).stream().map(this::toDto).toList();
    }

    private List<PreferenceDto> getAllPreferences(User user){
        return preferenceRepository.findAllByUser(user).stream().map(this::toDto).toList();
    }

    private PreferenceDto toDto(Preference preference) {
        return new PreferenceDto(preference.getId(), preference.getCategory().getName(), preference.getValue());
    }
}
