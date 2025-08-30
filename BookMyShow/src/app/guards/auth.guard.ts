import { inject } from '@angular/core';
import { CanActivateFn } from '@angular/router';
import { LoginService } from '../services/loginService/login.service';
import { Router } from '@angular/router';
import { map } from 'rxjs';

export const authGuard: CanActivateFn = (route, state) => {

    const userService = inject(LoginService);
    const router = inject(Router);

    return userService.isTokenValid().pipe(
      map(isValid => {
        if(isValid)
          return true;
        else
          router.navigate(['/login'], { queryParams: { returnUrl: state.url } } );
          return false;
      }));
};
