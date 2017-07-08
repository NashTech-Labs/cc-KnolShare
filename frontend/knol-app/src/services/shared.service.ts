import {Http, Headers} from "@angular/http";
import {Injectable} from "@angular/core";
import {Observable} from "rxjs/Observable";
import "rxjs/add/observable/of";
import "rxjs/add/observable/throw";
import "rxjs/add/operator/catch";
import "rxjs/add/operator/map";
import {Storage} from "@ionic/storage";

@Injectable()
export class SharedService {
  isLoggedIn: boolean = false;

  constructor(private http: Http, public storage: Storage) {
    this.storage.get("user").then((user) => {
      if (user) {
        this.storage.get("accessToken").then((accessToken) => {
          if (accessToken) {
            this.isLoggedIn = true;
          }
        });
      }
    });
  }

  clearStorage() {
    this.isLoggedIn = false;
    this.storage.clear().then(data => {
      console.log("Removed")
    })
  }

  logout(email: string, accessToken: string) {

    let headers = new Headers({
      "Content-Type": "application/json",
      "accessToken": accessToken,
      "email": email
    });

    return this.http.get("http://localhost:9000/knolshare/logout", {headers: headers})
      .map(res => this.extractData(res))
      .catch(this.handleError);

  }

  private extractData(res: any) {
    let body = res.json();
    return body || {};
  }

  /**
   * Handles error if there is an error in http request
   * @param error
   * @returns {ErrorObservable}
   */
  private handleError(error: any) {
    let errMsg: string;
    try {
      if (JSON.parse(error._body).error) {
        errMsg = JSON.parse(error._body).error.message;
      } else {
        errMsg = "Something went wrong. Please try again later.";
      }
    } catch (e) {
      errMsg = "Something went wrong. Please try again later.";
    }
    return Observable.throw(new Error(errMsg));
  }
}
