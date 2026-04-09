import { Routes } from '@angular/router';
import { BackupComponent } from './components/backup/backup.component';
import { LoginComponent } from './pages/login/login.component';
import { CreateCalledComponent } from './components/create-called/create-called.component';

export const routes: Routes = [
    {
        path: 'backup',
        component: BackupComponent
    },
    {
        path: 'login',
        component: LoginComponent
    },
    {
        path: "called",
        component: CreateCalledComponent
    }
];
