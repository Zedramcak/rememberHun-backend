package cz.adamzrcek.service;

import cz.adamzrcek.dtos.preference.PreferenceDto;
import cz.adamzrcek.dtos.preference.PreferenceNewRequest;
import cz.adamzrcek.entity.Preference;
import cz.adamzrcek.entity.PreferenceCategory;
import cz.adamzrcek.entity.Role;
import cz.adamzrcek.modules.user.entity.User;
import cz.adamzrcek.modules.auth.exception.NotAllowedException;
import cz.adamzrcek.exception.PreferenceNotFoundException;
import cz.adamzrcek.modules.user.service.UserService;
import cz.adamzrcek.repository.PreferenceCategoryRepository;
import cz.adamzrcek.repository.PreferenceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PreferenceServiceTest {

    @Mock
    private PreferenceRepository preferenceRepository;

    @Mock
    private UserService userService;

    @Mock
    private ConnectionService connectionService;

    @Mock
    private PreferenceCategoryRepository preferenceCategoryRepository;

    @InjectMocks
    private PreferenceService preferenceService;

    @Test
    public void getPreferenceOkTest() {
        Long id = 1L;
        User currentUser = User.builder()
                .id(8L)
                .username("username")
                .role(new Role(1L, "USER"))
                .build();

        when(preferenceRepository.findById(any())).thenReturn(Optional.of(Preference.builder().id(id).user(currentUser).build()));
        when(userService.getCurrentUser()).thenReturn(currentUser);
        given(preferenceCategoryRepository.findById(any())).willReturn(Optional.of(new PreferenceCategory(1L, "BRAND")));

        PreferenceDto preferenceDto = preferenceService.getPreference(id);

        assertEquals(id, preferenceDto.id());
    }

    @Test
    public void preferenceNotFoundTest() {
        Long id = 1L;

        given(preferenceRepository.findById(any())).willReturn(Optional.empty());

        assertThrows(PreferenceNotFoundException.class, () -> preferenceService.getPreference(id));

        verify(userService, never()).getCurrentUser();
    }

    @Test
    public void preferenceNotBelongingToCurrentUserTest() {
        Long id = 1L;
        User currentUser = User.builder()
                .id(8L)
                .username("username")
                .role(new Role(1L, "USER"))
                .build();
        User otherUser = TestUserFactory.basicUser();
        Preference currentUserPreference = Preference.builder().id(id).user(otherUser).build();

        given(preferenceRepository.findById(any())).willReturn(Optional.of(currentUserPreference));
        given(userService.getCurrentUser()).willReturn(currentUser);

        assertThrows(NotAllowedException.class, () -> preferenceService.getPreference(id));

    }

    @Test
    public void createPreferenceOkTest() {
        User currentUser = TestUserFactory.basicUser();
        var request = new PreferenceNewRequest(
                1L,
                "BRAND"
        );

        given(userService.getCurrentUser()).willReturn(currentUser);
        given(preferenceRepository.save(any()))
                .willReturn(
                        Preference.builder()
                        .id(1L)
                        .user(currentUser)
                        .category(new PreferenceCategory(1L, "BRAND"))
                        .value(request.getValue())
                        .build()
                );
        given(preferenceCategoryRepository.findById(any())).willReturn(Optional.of(new PreferenceCategory(1L, "BRAND")));

        PreferenceDto result = preferenceService.createPreference(request);

        ArgumentCaptor<Preference> preferenceArgumentCaptor = ArgumentCaptor.forClass(Preference.class);

        verify(userService, times(1)).getCurrentUser();
        verify(preferenceRepository, times(1)).save(preferenceArgumentCaptor.capture());

        Preference preference = preferenceArgumentCaptor.getValue();

        assertAll(
                () -> assertEquals(new PreferenceCategory(1L, "BRAND").getName(), result.category()),
                () -> assertEquals(request.getValue(), result.value()),
                () -> assertEquals(currentUser, preference.getUser()),
                () -> assertEquals(request.getCategoryId(), preference.getCategory().getId()),
                () -> assertEquals(request.getValue(), preference.getValue())
        );
    }


}