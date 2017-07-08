import { Component, OnInit } from "@angular/core";
import { NavController } from "ionic-angular";
import {YoutubeService} from "./youtube.service";
import { Storage } from "@ionic/storage";

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
  constructor(public storage: Storage) {}

  ngOnInit() {
    this.storage.get("videoData").then((val) => {
      if(val) {
        this.videoId = JSON.parse(val).id.videoId;
        this.title = JSON.parse(val).snippet.title;
        this.description = JSON.parse(val).snippet.description;
        this.player = new YT.Player('player', {
          videoId: this.videoId,
          playerVars: {
            rel: 0
          }
        });
      }
    });
  }

}
