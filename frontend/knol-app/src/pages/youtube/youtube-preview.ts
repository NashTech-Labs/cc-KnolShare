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

  ngOnInit() {
    this.player = new YT.Player('player', {
      videoId: 'videoId',
      playerVars: {
        rel: 0,
        controls: 0
      }
    });
  }

}
