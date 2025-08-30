import { HttpInterceptorFn } from '@angular/common/http';

export const authInterceptor: HttpInterceptorFn = (req, next) => {


  const excludedUrls = [
    '/login',
    '/register'
  ];

  // Check if request URL matches any excluded endpoint
  const isExcluded = excludedUrls.some(url => req.url.includes(url));
  if(isExcluded){
    return next(req);
  }

  console.log("Inside auth interceptor")

  const token = sessionStorage.getItem('access_token');
  if(token){
    const clonedReq = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });

    return next(clonedReq);
  }

  return next(req);
};
