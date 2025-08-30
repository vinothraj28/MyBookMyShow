import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, raceWith } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class MovieService {

  constructor(private http : HttpClient ) { }

  private registerUrl = 'http://localhost:8080/movies/register';
  private moviesUrl = 'http://localhost:8080/movies/list'
  private movieImageUrl = 'http://locaohost:8080/movies/image'

  registerMovie(data:{}):Observable<any>{
    return this.http.post(this.registerUrl, data);
  }

  getMovies():Observable<any>{
    return this.http.get(this.moviesUrl);
  }

  getMovieImage(id:number):Observable<Blob>{

    const url = `http://localhost:8080/movies/image/${id}`;
    return this.http.get(url, {responseType:'blob'});

  }

}
