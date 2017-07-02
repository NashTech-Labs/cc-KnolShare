import { Component, OnInit } from "@angular/core";
import { NavController } from "ionic-angular";
import {YoutubeService} from "./youtube.service";

@Component({
  selector: "page-youtube",
  template: "<h1>Youtube page</h1>",
  providers: [YoutubeService]
})
export class YoutubePage implements OnInit{

  constructor(public navCtrl: NavController,
              private youtubeService: YoutubeService) {}

  ngOnInit() {
    this.youtubeService.getVideos().subscribe((data: any) => {
      console.log(data);
    }, (err: any) => {
      console.error(err);
    })
  }
}
