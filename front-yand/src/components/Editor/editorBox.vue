<template>
  <div style="border: 1px solid #ccc;">
    <Toolbar
        style="border-bottom: 1px solid #ccc"
        :editor="editor"
        :defaultConfig="toolbarConfig"
        :mode="mode"
    />
    <Editor
        style="height: 100vh; overflow-y: hidden;"
        v-model="reqbody.articleText"
        :defaultConfig="editorConfig"
        :mode="mode"
        @onCreated="onCreated"
    />
  </div>
</template>

<script>
import { Editor, Toolbar } from '@wangeditor/editor-for-vue'
import "@wangeditor/editor/dist/css/style.css"
import {editorConfig} from "@/config/CommonConfig/EditorConfig";
import { Boot } from '@wangeditor/editor'
import markdownModule from '@wangeditor/plugin-md'

Boot.registerModule(markdownModule)
export default {
  props:{
    url:null
  },
  name: "editorBox",
  components: { Editor, Toolbar },
  data() {
    return {
      reqbody:this.$parent.$parent.$parent.$parent.articleAddRequest,
      editor: null,
      toolbarConfig: {},
      editorConfig: editorConfig,
      mode: 'default', // or 'simple'
    }
  },
  methods: {
    onCreated(editor) {
      this.editor = Object.seal(editor) // 一定要用 Object.seal() ，否则会报错
      this.editor.getConfig();
    }
  },
  mounted() {
    if (this.url != null) {
      this.reqbody = this.$parent.$parent.$parent.$parent.articleUpdateRequest
      var xhr = new XMLHttpRequest();
      xhr.open('GET',this.url,false);
      xhr.send(null);
      this.reqbody.articleText=xhr.responseText
    }
    // 模拟 ajax 请求，异步渲染编辑器
    // setTimeout(() => {
    //
    // })
  },
  beforeDestroy() {
    const editor = this.editor
    if (editor == null) return
    editor.destroy() // 组件销毁时，及时销毁编辑器
  }
}
</script>

<style scoped>

</style>