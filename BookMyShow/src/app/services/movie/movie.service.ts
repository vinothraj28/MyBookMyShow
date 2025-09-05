import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, raceWith } from 'rxjs';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class MovieService {

  constructor(private http : HttpClient ) { }

  private baseUrl = environment.apiUrl;
  private registerUrl = `${this.baseUrl}/movies/register`;
  private moviesUrl = `${this.baseUrl}/movies/list`;
  private movieImageUrl = `${this.baseUrl}/movies/image`;

  registerMovie(data:{}):Observable<any>{
    return this.http.post(this.registerUrl, data);
  }

  getMovies():Observable<any>{
    return this.http.get(this.moviesUrl);
  }

  getMovieImage(id:number):Observable<Blob>{

    const url = `${this.baseUrl}/movies/image/${id}`;
    return this.http.get(url, {responseType:'blob'});

  }

}
