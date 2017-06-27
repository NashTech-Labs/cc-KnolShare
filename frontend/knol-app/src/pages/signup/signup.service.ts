import {Http, Headers} from "@angular/http";
import {Injectable} from "@angular/core";
import {Observable} from "rxjs/Observable";
import "rxjs/add/observable/of";
import "rxjs/add/observable/throw";
import "rxjs/add/operator/catch";
import "rxjs/add/operator/map";
import {SignupForm} from "../../models/signup-form";

@Injectable()
export class SignupService {
  constructor(private http: Http) {}

  signup(signupData: SignupForm) {
    let headers = new Headers({
      "Content-Type": "application/json"
    });

    return this.http.post("http://localhost:9000/knolshare/register", signupData, {headers: headers})
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
      if (JSON.parse(error._body).message) {
        errMsg = JSON.parse(error._body).message;
      } else {
        errMsg = "Something went wrong. Please try again later.";
      }
    } catch(e) {
      errMsg = "Something went wrong. Please try again later.";
    }
    return Observable.throw(new Error(errMsg));
  }
}
