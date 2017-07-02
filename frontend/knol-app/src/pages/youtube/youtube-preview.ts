import { Component, OnInit } from "@angular/core";
import { NavController } from "ionic-angular";
import {YoutubeService} from "./youtube.service";

declare var YT: any;

@Component({
  selector: "page-youtube-preview",
  templateUrl: "youtube-preview.html"
  //providers: [YoutubeService]
})
export class YoutubePreviewPage implements OnInit {
  player: any;
  title: string;
  description: string;
  videoId: string;
  ngOnInit() {
    if(localStorage.getItem("videoData")) {
      this.videoId = JSON.parse(localStorage.getItem("videoData")).id.videoId;
      this.title = JSON.parse(localStorage.getItem("videoData")).snippet.title;
      this.description = JSON.parse(localStorage.getItem("videoData")).snippet.description;
      this.player = new YT.Player('player', {
        videoId: this.videoId,
        playerVars: {
          rel: 0
        }
      });
    }

  }

}
