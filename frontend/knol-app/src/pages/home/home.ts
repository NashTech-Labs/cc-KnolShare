import { Component, OnInit } from "@angular/core";
import { NavController } from "ionic-angular";
import {LoginPage} from "../login/login";
import {SignupPage} from "../signup/signup";
import {YoutubePage} from "../youtube/youtube";
import {SharedService} from "../../services/shared.service";
import { Storage } from "@ionic/storage";


@Component({
  selector: "page-home",
  templateUrl: "home.html"
})
export class HomePage implements OnInit {

  userName: string;
  pages = {
    "Login": LoginPage,
    "Signup": SignupPage,
    "Youtube": YoutubePage
  };

  constructor(public navCtrl: NavController,
              private sharedService: SharedService,
              public storage: Storage) {}

  ngOnInit() {
    if (this.sharedService.isLoggedIn) {
      this.storage.get("user").then((val) => {
        if(val) {
          this.userName = JSON.parse(val).userName;
        }
      });
    }
  }

  goToPage(page: string) {
    this.navCtrl.push(this.pages[page]);
  }

}
