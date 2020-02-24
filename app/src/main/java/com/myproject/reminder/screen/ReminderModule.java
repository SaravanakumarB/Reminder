package com.myproject.reminder.screen;

import androidx.lifecycle.ViewModelProvider;

import com.myproject.reminder.retrofit.NetworkAPI;
import com.myproject.reminder.util.ViewModelProviderFactory;

import dagger.Module;
import dagger.Provides;

@Module
public class ReminderModule {
    @Provides
    ReminderViewModel providesViewModel(ReminderRepository repository) {
        return new ReminderViewModel(repository);
    }

    @Provides
    ReminderRepository providesRepository(NetworkAPI networkAPI) {
        return new ReminderRepository(networkAPI);
    }

    @Provides
    ViewModelProvider.Factory provideViewModelProvider(ReminderViewModel viewModel) {
        return new ViewModelProviderFactory<>(viewModel);
    }
}
