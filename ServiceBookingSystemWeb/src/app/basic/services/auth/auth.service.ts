import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { map, Observable } from 'rxjs';
import { UserStorageService } from '../storage/user-storage.service';

const BASIC_URL = 'http://localhost:8080/';
const AUTH_HEADER = 'authorization';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  constructor(
    private http: HttpClient,
    private userStorageService: UserStorageService
  ) {}

  registerClient(signupRequestDTO: any): Observable<any> {
    return this.http.post(BASIC_URL + 'client/sign-up', signupRequestDTO);
  }

  registerCompany(signupRequestDTO: any): Observable<any> {
    return this.http.post(BASIC_URL + 'company/sign-up', signupRequestDTO);
  }

  login(username: string, password: string) {
    return this.http
      .post(
        BASIC_URL + 'authenticate',
        { username, password },
        { observe: 'response' }
      )
      .pipe(
        map((res: HttpResponse<any>) => {
          const tokenLength = res.headers.get(AUTH_HEADER)?.length;
          const bearerToken = res.headers
            .get(AUTH_HEADER)
            ?.substring(7, tokenLength); // Supprime "Bearer "

          this.userStorageService.saveUser(res.body);
          if (bearerToken) {
            this.userStorageService.saveToken(bearerToken);
          }
          return res;
        })
      );
  }
}
