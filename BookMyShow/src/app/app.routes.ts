import { Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { BaseComponent } from './base/base.component';
import { HomeComponent } from './home/home.component';
import { RegisterComponent } from './register/register.component';
import { authGuard } from './guards/auth.guard';
import { TheaterComponent } from './theater/theater.component';
import { BaseNavComponent } from './base-nav/base-nav.component';
import { TheaterListComponent } from './theater-list/theater-list.component';
import { MovieComponent } from './movie/movie.component';
import { ShowTimesComponent } from './show-times/show-times.component';
import { BookingPageComponent } from './booking-page/booking-page.component';
import { TicketDownloadComponent } from './ticket-download/ticket-download.component';
import { PortfolioComponent } from './portfolio/portfolio.component';

export const routes: Routes = [

    {path:'' , redirectTo:'login', pathMatch:'full'},
    {path:'portfolio', component:PortfolioComponent},
    {path:'login', component:LoginComponent},
    {path:'register', component:RegisterComponent},
    {path:'base', component:BaseComponent,  canActivate: [authGuard],
        children:[
            {path:'', redirectTo:'home', pathMatch:'full'},
            {path:'home', component:HomeComponent},
            
            {path:'theater', component:BaseNavComponent,
                children: [
                    {path:'', redirectTo:'register', pathMatch:'full'},
                    {path:'register', component:TheaterComponent},
                    {path:'list', component:TheaterListComponent},
                    {path:'movie', component:MovieComponent},
                    {path:'showtimes', component:ShowTimesComponent}
                ]
            },
            {path:'book', component:BookingPageComponent},
            {path:'ticket-download', component: TicketDownloadComponent}

        ]
    }

];
