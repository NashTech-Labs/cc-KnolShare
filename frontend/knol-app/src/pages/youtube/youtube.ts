import { Component, OnInit } from "@angular/core";
import { NavController } from "ionic-angular";
import {YoutubeService} from "./youtube.service";
import {YoutubePreviewPage} from "./youtube-preview";
import { Storage } from "@ionic/storage";

@Component({
  selector: "page-youtube",
  templateUrl: "youtube.html",
  providers: [YoutubeService]
})
export class YoutubePage implements OnInit{

  items: any[];
  youtubeData: any;
  ids: string[] = [];
  constructor(public navCtrl: NavController,
              private youtubeService: YoutubeService,
              public storage: Storage) {}

  ngOnInit() {
    this.youtubeService.getVideos().subscribe((data: any) => {
      this.items = data.items;
      this.youtubeData = data;
      for(let i=0; i < this.items.length; i++) {
        if(this.items[i].id) {
          if(this.items[i].id.videoId) {
            this.ids.push = this.items[i].id.videoId;
          }
        }
      }
    }, (err: any) => {
      alert(err);
      console.error(err);
    })
  }

  goToPreviewPage(i: number) {
    this.storage.get("videoData").then((val) => {
      if(val) {
        this.storage.remove("videoData").then((res) => {
          this.storage.set('videoData', JSON.stringify(this.items[i]));
        })
      } else {
        this.storage.set('videoData', JSON.stringify(this.items[i]));
      }
    }, err => console.error(err));

    this.navCtrl.push(YoutubePreviewPage);
  }

  previousPage(){
    this.youtubeService.getVideos(null, this.youtubeData.prevPageToken).subscribe((data: any) => {
      this.items = data.items;
      this.youtubeData = data;
      for(let i=0; i < this.items.length; i++) {
        if(this.items[i].id) {
          if(this.items[i].id.videoId) {
            this.ids.push = this.items[i].id.videoId;
          }
        }
      }
    }, (err: any) => {
      alert(err);
      console.error(err);
    })
  }

  nextPage() {
    this.youtubeService.getVideos(this.youtubeData.nextPageToken).subscribe((data: any) => {
      this.items = data.items;
      this.youtubeData = data;
      for(let i=0; i < this.items.length; i++) {
        if(this.items[i].id) {
          if(this.items[i].id.videoId) {
            this.ids.push = this.items[i].id.videoId;
          }
        }
      }
    }, (err: any) => {
      alert(err);
      console.error(err);
    })
  }
}
