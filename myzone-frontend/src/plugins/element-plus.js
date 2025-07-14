import 'element-plus/dist/index.css'
import {
  ElButton,
  ElInput,
  ElIcon,
  ElDivider,
  ElEmpty,
  ElTag,
  ElContainer,
  ElHeader,
  ElMain,
  ElCard,
  ElMenu,
  ElMenuItem,
  ElDropdown,
  ElDropdownMenu,
  ElDropdownItem,
  ElForm,
  ElFormItem,
  ElSelect,
  ElOption,
  ElCarousel,
  ElCarouselItem,
  ElPagination,
  ElSkeleton,
  ElAvatar,
  ElMessage,
  ElMessageBox,
  ElProgress,
  ElUpload,
  ElDialog,
  ElRadio,
  ElRadioGroup,
  ElRadioButton
} from 'element-plus'

import {
  HomeFilled,
  Location,
  UploadFilled,
  Delete,
  User,
  SwitchButton,
  Picture,
  Plus,
  CircleCheckFilled,
  UserFilled,
  Lock,
  Message,
  Document
} from '@element-plus/icons-vue'

// 按需导入的组件
const components = [
  ElButton,
  ElInput,
  ElIcon,
  ElDivider,
  ElEmpty,
  ElTag,
  ElContainer,
  ElHeader,
  ElMain,
  ElCard,
  ElMenu,
  ElMenuItem,
  ElDropdown,
  ElDropdownMenu,
  ElDropdownItem,
  ElForm,
  ElFormItem,
  ElSelect,
  ElOption,
  ElCarousel,
  ElCarouselItem,
  ElPagination,
  ElSkeleton,
  ElAvatar,
  ElProgress,
  ElUpload,
  ElDialog,
  ElRadio,
  ElRadioGroup,
  ElRadioButton
]

// 按需导入的图标
const icons = [
  HomeFilled,
  Location,
  UploadFilled,
  Delete,
  User,
  SwitchButton,
  Picture,
  Plus,
  CircleCheckFilled,
  UserFilled,
  Lock,
  Message,
  Document
]

export default {
  install(app) {
    // 注册组件
    components.forEach(component => {
      app.component(component.name, component)
    })
    
    // 注册图标
    icons.forEach(icon => {
      app.component(icon.name, icon)
    })
    
    // 全局属性
    app.config.globalProperties.$message = ElMessage
    app.config.globalProperties.$messageBox = ElMessageBox
  }
}

// 导出服务组件，供组件内使用
export { ElMessage, ElMessageBox } 