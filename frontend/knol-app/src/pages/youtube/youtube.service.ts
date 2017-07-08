import {Http, Headers} from "@angular/http";
import {Injectable} from "@angular/core";
import {Observable} from "rxjs/Observable";
import "rxjs/add/observable/of";
import "rxjs/add/observable/throw";
import "rxjs/add/operator/catch";
import "rxjs/add/operator/map";
import {SignupForm} from "../../models/signup-form";

@Injectable()
export class YoutubeService {
  constructor(private http: Http) {}

  getVideos(nextPageToken?: string, previousPageToken?: string) {
    let headers = new Headers({
      "Content-Type": "application/json"
    });

    let url;

    if (nextPageToken) {
      url = `https://www.googleapis.com/youtube/v3/search?pageToken=${nextPageToken}&order=date&part=snippet&channelId=UCP4g5qGeUSY7OokXfim1QCQ&key=AIzaSyCmedFZ2QVVzQ1cElmU6kPM2PV5YEaQwhY`;
    } else if (previousPageToken) {
      url = `https://www.googleapis.com/youtube/v3/search?pageToken=${previousPageToken}&order=date&part=snippet&channelId=UCP4g5qGeUSY7OokXfim1QCQ&key=AIzaSyCmedFZ2QVVzQ1cElmU6kPM2PV5YEaQwhY`;
    } else {
      url = "https://www.googleapis.com/youtube/v3/search?order=date&part=snippet&channelId=UCP4g5qGeUSY7OokXfim1QCQ&key=AIzaSyCmedFZ2QVVzQ1cElmU6kPM2PV5YEaQwhY";
    }
    return this.http.get(url, {headers: headers})
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
